package dasturlash.uz.service.auth;

import dasturlash.uz.dtos.auth.RegistrationDTO;
import dasturlash.uz.enums.LanguageEnum;
import dasturlash.uz.enums.SmsStatus;
import dasturlash.uz.service.ResourceBundleService;
import org.springframework.stereotype.Service;

// SmsAuthService.java


import dasturlash.uz.entity.Profile;
import dasturlash.uz.entity.SmsHistory;
import dasturlash.uz.enums.Status;
import dasturlash.uz.exceptions.DataNotFoundException;
import dasturlash.uz.repository.ProfileRepository;
import dasturlash.uz.repository.SmsHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class SmsAuthService {
    private final ProfileRepository profileRepository;
    private final SmsHistoryRepository smsHistoryRepository;
    private final MockSmsService mockSmsService;
    private final MessageHistoryService messageHistoryService;
    private final ResourceBundleService resourceBundleService;

    @Value("${sms.confirmation.code.length:6}")
    private int smsCodeLength;

    @Value("${sms.confirmation.deadline.minutes:5}")
    private int smsConfirmationDeadlineMinutes;

    @Value("${sms.max.resend.attempts:3}")
    private int smsMaxResendAttempts;


    public String registerViaSms(RegistrationDTO dto, Profile profile, LanguageEnum lang) {
        String normalizedPhone = normalizePhoneNumber(dto.getLogin());
        profile.setPhone(normalizedPhone);
        profileRepository.save(profile);

        String verificationCode = generateVerificationCode();

        // Create history record via service
        SmsHistory smsHistory = messageHistoryService.createSmsHistory(normalizedPhone, verificationCode);

        String messagePattern = resourceBundleService.getMessage("sms.confirmation.message", lang);
        String message = String.format(messagePattern, verificationCode, smsConfirmationDeadlineMinutes);


        mockSmsService.sendSms(normalizedPhone, message, verificationCode);
        // Note: MockSmsService will handle updating the history status

        return resourceBundleService.getMessage("sms.confirmation.sent", lang);
    }

    public String confirmSms(String phone, String code, LanguageEnum lang) {
        String normalizedPhone = normalizePhoneNumber(phone);
        Profile profile = profileRepository.findByPhoneAndVisibleTrue(normalizedPhone)
                .orElseThrow(() -> new DataNotFoundException("Profile not found"));

        SmsHistory smsHistory = smsHistoryRepository.findTopByPhoneOrderByCreatedDateDesc(normalizedPhone)
                .orElseThrow(() -> new DataNotFoundException("No verification attempt found"));

        LocalDateTime expiryTime = smsHistory.getCreatedDate().plusMinutes(smsConfirmationDeadlineMinutes);
        if (LocalDateTime.now().isAfter(expiryTime)) {
            messageHistoryService.updateSmsStatus(smsHistory, SmsStatus.EXPIRED);
            throw new DataNotFoundException(resourceBundleService.getMessage("sms.confirmation.code.expired", lang));
        }

        if (!smsHistory.getVerificationCode().equals(code)) {
            messageHistoryService.updateSmsStatus(smsHistory, SmsStatus.FAILED);
            throw new DataNotFoundException(resourceBundleService.getMessage("sms.confirmation.code.wrong", lang));
        }

        messageHistoryService.updateSmsStatus(smsHistory, SmsStatus.DELIVERED);
        profile.setStatus(Status.ACTIVE);
        profileRepository.save(profile);

        return resourceBundleService.getMessage("sms.confirmation.success", lang);
    }

    public String resendSmsVerification(String phone, LanguageEnum lang) {
        String normalizedPhone = normalizePhoneNumber(phone);
        Profile profile = profileRepository.findByPhoneAndVisibleTrue(normalizedPhone)
                .orElseThrow(() -> new DataNotFoundException("Profile not found"));

        SmsHistory lastHistory = smsHistoryRepository.findTopByPhoneOrderByCreatedDateDesc(normalizedPhone)
                .orElseThrow(() -> new DataNotFoundException("No previous SMS verification found"));

        if (profile.getStatus().equals(Status.ACTIVE)) {
            messageHistoryService.updateSmsStatus(lastHistory, SmsStatus.FAILED);
            return resourceBundleService.getMessage("login.profile.already.active", lang);
        }

        if (lastHistory.getAttemptCount() >= smsMaxResendAttempts) {
            messageHistoryService.updateSmsStatus(lastHistory, SmsStatus.FAILED);
            profile.setStatus(Status.BLOCKED);
            profileRepository.save(profile);
            throw new DataNotFoundException(resourceBundleService.getMessage("sms.max.resend.attempts.exceeded", lang));
        }

        String newVerificationCode = generateVerificationCode();

        // Create new history record for resend
        SmsHistory newHistory = messageHistoryService.createSmsHistory(normalizedPhone, newVerificationCode);
        newHistory.setAttemptCount(lastHistory.getAttemptCount() + 1);
        messageHistoryService.updateSmsStatus(newHistory, SmsStatus.RESENT);

        String messagePattern = resourceBundleService.getMessage("sms.confirmation.message", lang);
        String message = String.format(messagePattern, newVerificationCode, smsConfirmationDeadlineMinutes);
        mockSmsService.sendSms(normalizedPhone, message, newVerificationCode);

        return resourceBundleService.getMessage("sms.confirmation.resent", lang);
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
