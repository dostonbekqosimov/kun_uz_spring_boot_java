package dasturlash.uz.service;

import dasturlash.uz.dtos.profileDTOs.RegistrationDTO;
import dasturlash.uz.enums.SmsStatus;
import org.springframework.stereotype.Service;

// SmsAuthService.java


import dasturlash.uz.dtos.profileDTOs.RegistrationDTO;
import dasturlash.uz.entity.Profile;
import dasturlash.uz.entity.SmsHistory;
import dasturlash.uz.enums.Status;
import dasturlash.uz.exceptions.DataNotFoundException;
import dasturlash.uz.repository.ProfileRepository;
import dasturlash.uz.repository.SmsHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class SmsAuthService {
    private final ProfileRepository profileRepository;
    private final SmsHistoryRepository smsHistoryRepository;
    private final MockSmsService mockSmsService;
    private final MessageHistoryService messageHistoryService;

    @Value("${sms.confirmation.code.length:6}")
    private int smsCodeLength;

    @Value("${sms.confirmation.deadline.minutes:5}")
    private int smsConfirmationDeadlineMinutes;

    @Value("${sms.max.resend.attempts:3}")
    private int smsMaxResendAttempts;


    public String registerViaSms(RegistrationDTO dto, Profile profile) {
        String normalizedPhone = normalizePhoneNumber(dto.getLogin());
        profile.setPhone(normalizedPhone);
        profileRepository.save(profile);

        String verificationCode = generateVerificationCode();

        // Create history record via service
        SmsHistory smsHistory = messageHistoryService.createSmsHistory(normalizedPhone, verificationCode);

        String message = String.format("Your verification code is: %s. Valid for %d minutes.",
                verificationCode, smsConfirmationDeadlineMinutes);

        mockSmsService.sendSms(normalizedPhone, message, verificationCode);
        // Note: MockSmsService will handle updating the history status

        return "SMS verification sent";
    }

    public String confirmSms(String phone, String code) {
        String normalizedPhone = normalizePhoneNumber(phone);
        Profile profile = profileRepository.findByPhoneAndVisibleTrue(normalizedPhone)
                .orElseThrow(() -> new DataNotFoundException("Profile not found"));

        SmsHistory smsHistory = smsHistoryRepository.findTopByPhoneOrderByCreatedDateDesc(normalizedPhone)
                .orElseThrow(() -> new DataNotFoundException("No verification attempt found"));

        LocalDateTime expiryTime = smsHistory.getCreatedDate().plusMinutes(smsConfirmationDeadlineMinutes);
        if (LocalDateTime.now().isAfter(expiryTime)) {
            messageHistoryService.updateSmsStatus(smsHistory, SmsStatus.EXPIRED);
            throw new DataNotFoundException("Verification code has expired");
        }

        if (!smsHistory.getVerificationCode().equals(code)) {
            messageHistoryService.updateSmsStatus(smsHistory, SmsStatus.FAILED);
            throw new DataNotFoundException("Invalid verification code");
        }

        messageHistoryService.updateSmsStatus(smsHistory, SmsStatus.DELIVERED);
        profile.setStatus(Status.ACTIVE);
        profileRepository.save(profile);

        return "Registration confirmed successfully";
    }

    public String resendSmsVerification(String phone) {
        String normalizedPhone = normalizePhoneNumber(phone);
        Profile profile = profileRepository.findByPhoneAndVisibleTrue(normalizedPhone)
                .orElseThrow(() -> new DataNotFoundException("Profile not found"));

        SmsHistory lastHistory = smsHistoryRepository.findTopByPhoneOrderByCreatedDateDesc(normalizedPhone)
                .orElseThrow(() -> new DataNotFoundException("No previous SMS verification found"));

        if (profile.getStatus().equals(Status.ACTIVE)) {
            messageHistoryService.updateSmsStatus(lastHistory, SmsStatus.FAILED);
            return "Profile is already active";
        }

        if (lastHistory.getAttemptCount() >= smsMaxResendAttempts) {
            messageHistoryService.updateSmsStatus(lastHistory, SmsStatus.FAILED);
            profile.setStatus(Status.BLOCKED);
            profileRepository.save(profile);
            throw new DataNotFoundException("Maximum resend attempts exceeded");
        }

        String newVerificationCode = generateVerificationCode();

        // Create new history record for resend
        SmsHistory newHistory = messageHistoryService.createSmsHistory(normalizedPhone, newVerificationCode);
        newHistory.setAttemptCount(lastHistory.getAttemptCount() + 1);
        messageHistoryService.updateSmsStatus(newHistory, SmsStatus.RESENT);

        String message = String.format("Your new verification code is: %s. Valid for %d minutes.",
                newVerificationCode, smsConfirmationDeadlineMinutes);
        mockSmsService.sendSms(normalizedPhone, message, newVerificationCode);

        return "New verification code sent";
    }

    private String generateVerificationCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < smsCodeLength; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }

    private String normalizePhoneNumber(String phone) {
        // Remove any whitespace
        String normalized = phone.replaceAll("\\s+", "");

        // Ensure it starts with "+"
        if (!normalized.startsWith("+")) {
            normalized = "+" + normalized;
        }

        // Remove any other special characters
        normalized = normalized.replaceAll("[^+0-9]", "");

        return normalized;
    }
}
