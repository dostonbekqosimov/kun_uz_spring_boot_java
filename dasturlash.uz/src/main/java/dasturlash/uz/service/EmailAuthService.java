package dasturlash.uz.service;

import dasturlash.uz.enums.EmailStatus;
import dasturlash.uz.enums.SmsStatus;
import org.springframework.stereotype.Service;


import dasturlash.uz.dtos.profileDTOs.MessageDTO;
import dasturlash.uz.dtos.profileDTOs.RegistrationDTO;
import dasturlash.uz.entity.EmailHistory;
import dasturlash.uz.entity.Profile;
import dasturlash.uz.enums.Role;
import dasturlash.uz.enums.Status;
import dasturlash.uz.exceptions.DataNotFoundException;
import dasturlash.uz.repository.EmailHistoryRepository;
import dasturlash.uz.repository.ProfileRepository;
import dasturlash.uz.util.MD5Util;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailAuthService {
    private final ProfileRepository profileRepository;
    private final EmailHistoryRepository emailHistoryRepository;
    private final EmailSendingService emailSendingService;
    private final EmailTemplateService emailTemplateService;
    private final MessageHistoryService messageHistoryService;

    @Value("${registration.confirmation.deadline.minutes}")
    private int confirmationDeadlineMinutes;

    @Value("${registration.max.resend.attempts}")
    private int maxResendAttempts;

    public String registerViaEmail(RegistrationDTO dto, Profile profile) {
        profile.setEmail(dto.getLogin());
        profileRepository.save(profile);

        String emailContent = emailTemplateService.getRegistrationEmailTemplate(
                profile.getId(),
                profile.getName(),
                confirmationDeadlineMinutes,
                maxResendAttempts
        );

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setToAccount(dto.getLogin());
        messageDTO.setSubject("Complete your registration");
        messageDTO.setText(emailContent);

        emailSendingService.sendMimeMessage(messageDTO, profile);
        // Note: EmailSendingService will handle creating and updating the history

        return "Email verification sent";
    }

    public String confirmEmail(Long id) {
        Profile profile = profileRepository.findByIdAndVisibleTrue(id)
                .orElseThrow(() -> new DataNotFoundException("Profile not found"));

        EmailHistory emailHistory = emailHistoryRepository.findTopByEmailOrderByCreatedDateDesc(profile.getEmail())
                .orElseThrow(() -> new DataNotFoundException("No email history found"));

        LocalDateTime exp = LocalDateTime.now().minusMinutes(confirmationDeadlineMinutes);
        if (exp.isAfter(emailHistory.getCreatedDate())) {
            profile.setStatus(Status.IN_REGISTRATION);
            messageHistoryService.updateEmailStatus(emailHistory, EmailStatus.EXPIRED);
            profileRepository.save(profile);
            return "Confirmation link has expired. Please request a new confirmation email.";
        }

        if (!profile.getStatus().equals(Status.IN_REGISTRATION)) {
            messageHistoryService.updateEmailStatus(emailHistory, EmailStatus.FAILED);
            return "Not Completed";
        }

        profile.setStatus(Status.ACTIVE);
        messageHistoryService.updateEmailStatus(emailHistory, EmailStatus.DELIVERED);
        profileRepository.save(profile);
        return "Completed";
    }

    public String resendEmailConfirmation(Long id) {
        Profile profile = profileRepository.findByIdAndVisibleTrue(id)
                .orElseThrow(() -> new DataNotFoundException("Profile not found"));

        EmailHistory lastHistory = emailHistoryRepository.findTopByEmailOrderByCreatedDateDesc(profile.getEmail())
                .orElseThrow(() -> new DataNotFoundException("No email history found"));

        if (lastHistory.getAttemptCount() >= maxResendAttempts) {
            messageHistoryService.updateEmailStatus(lastHistory, EmailStatus.FAILED);
            profile.setStatus(Status.BLOCKED);
            profileRepository.save(profile);
            throw new DataNotFoundException("Maximum resend attempts exceeded. Please contact support.");
        }

        if (profile.getStatus().equals(Status.ACTIVE)) {
            messageHistoryService.updateEmailStatus(lastHistory, EmailStatus.FAILED);
            return "Profile is already active";
        }

        String emailContent = emailTemplateService.getRegistrationEmailTemplate(
                profile.getId(),
                profile.getName(),
                confirmationDeadlineMinutes,
                maxResendAttempts - lastHistory.getAttemptCount()
        );

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setToAccount(profile.getEmail());
        messageDTO.setSubject("Registration Confirmation - New Link");
        messageDTO.setText(emailContent);

        emailSendingService.sendMimeMessage(messageDTO, profile);
        // Note: EmailSendingService will handle creating and updating the history

        return "New confirmation email sent";
    }
}