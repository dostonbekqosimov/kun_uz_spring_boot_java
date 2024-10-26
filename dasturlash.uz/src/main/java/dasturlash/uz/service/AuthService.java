package dasturlash.uz.service;


import dasturlash.uz.dtos.profileDTOs.MessageDTO;
import dasturlash.uz.dtos.profileDTOs.RegistrationDTO;
import dasturlash.uz.entity.Profile;
import dasturlash.uz.enums.Role;
import dasturlash.uz.enums.Status;
import dasturlash.uz.exceptions.DataExistsException;
import dasturlash.uz.exceptions.DataNotFoundException;
import dasturlash.uz.repository.ProfileRepository;
import dasturlash.uz.util.MD5Util;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {


    @Value("${registration.confirmation.deadline.minutes}")
    private int confirmationDeadlineMinutes;
    @Value("${registration.max.resend.attempts}")
    private int maxResendAttempts;


    private final ProfileRepository profileRepository;
    private final EmailSendingService emailSendingService;
    private final EmailTemplateService emailTemplateService;

    public String registration(RegistrationDTO dto) {
        // check email exists
        existsByEmailOrPhone(dto.getEmail(), dto.getPhone());

        Profile entity = new Profile();
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setPassword(MD5Util.getMd5(dto.getPassword()));
        entity.setSurname(dto.getSurname());
        entity.setCreatedAt(LocalDateTime.now());

        entity.setRole(Role.USER);
        entity.setVisible(Boolean.TRUE);
        entity.setEmailConfirmationDeadline(LocalDateTime.now().plusMinutes(confirmationDeadlineMinutes));
        entity.setStatus(Status.IN_REGISTRATION);
        profileRepository.save(entity);

        // Generate email content using the template service
        String emailContent = emailTemplateService.getRegistrationEmailTemplate(
                entity.getId(),
                entity.getName(),
                confirmationDeadlineMinutes,
                maxResendAttempts
        );

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setToAccount(dto.getEmail());
        messageDTO.setSubject("Complete your registration");
        messageDTO.setText(emailContent);

        emailSendingService.sendMimeMessage(messageDTO, entity);

        return "Email was sent";
    }

    public String registrationConfirm(Long id) {

        Optional<Profile> optional = profileRepository.findByIdAndVisibleTrue(id);
        if (optional.isEmpty()) {
            return "Not Completed";
        }

        Profile entity = optional.get();

        // Check if confirmation period has expired
        if (LocalDateTime.now().isAfter(entity.getEmailConfirmationDeadline())) {
            entity.setStatus(Status.IN_REGISTRATION);
            profileRepository.save(entity);
            return "Confirmation link has expired. Please request a new confirmation email.";
        }

        if (!entity.getStatus().equals(Status.IN_REGISTRATION)) {
            return "Not Completed";
        }

        entity.setStatus(Status.ACTIVE);
        profileRepository.save(entity);
        return "Completed";
    }

    public String resendConfirmationEmail(Long id) {
        Optional<Profile> optional = profileRepository.findByIdAndVisibleTrue(id);
        if (optional.isEmpty()) {
            throw new DataNotFoundException("Profile not found");
        }

        Profile entity = optional.get();

        // Check if profile is already active
        if (entity.getStatus().equals(Status.ACTIVE)) {
            return "Profile is already active";
        }

        // Check if profile has exceeded maximum resend attempts
        if (entity.getResendAttempts() >= maxResendAttempts) {
            entity.setStatus(Status.BLOCKED);
            profileRepository.save(entity);
            throw new DataNotFoundException("Maximum resend attempts exceeded. Please contact support.");
        }

        // Reset confirmation deadline and increment resend attempts
        entity.setEmailConfirmationDeadline(LocalDateTime.now().plusMinutes(confirmationDeadlineMinutes));
        entity.setResendAttempts(entity.getResendAttempts() + 1);
        entity.setStatus(Status.IN_REGISTRATION);
        profileRepository.save(entity);

        // Send new confirmation email
        String emailContent = emailTemplateService.getRegistrationEmailTemplate(
                entity.getId(),
                entity.getName(),
                confirmationDeadlineMinutes,
                maxResendAttempts - entity.getResendAttempts()  // Pass remaining attempts
        );

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setToAccount(entity.getEmail());
        messageDTO.setSubject("Registration Confirmation - New Link");
        messageDTO.setText(emailContent);

        emailSendingService.sendMimeMessage(messageDTO, entity);

        return "New confirmation email sent. Please check your inbox.";
    }


    public String login(String email, String password) {

        // Find the user by email
        Optional<Profile> optional = profileRepository.findByEmailAndVisibleTrue(email);
        if (optional.isEmpty()) {
            throw new DataNotFoundException("Email not found");
        }

        Profile entity = optional.get();

        // Check if the password matches
        if (!entity.getPassword().equals(MD5Util.getMd5(password))) {
            // buyerda aynan nima xatoligini bilintirib qo'ymaslik kerak
            throw new DataNotFoundException("Invalid password");
        }

        // Check if the profile is active
        if (!entity.getStatus().equals(Status.ACTIVE)) {
            throw new DataNotFoundException("Account is not active");
        }

        // Login successful
        return "Login successful";
    }

    private void existsByEmailOrPhone(String email, String phone) {

        if (email != null && !email.trim().isEmpty()) {
            boolean isEmailExist = profileRepository.existsByEmailAndVisibleTrue(email);
            if (isEmailExist) {
                throw new DataExistsException("Profile with email: " + email + " already exists");
            }
        }

        if (phone != null && !phone.trim().isEmpty()) {
            boolean isPhoneExist = profileRepository.existsByPhoneAndVisibleTrue(phone);
            if (isPhoneExist) {
                throw new DataExistsException("Profile with phone: " + phone + " already exists");
            }
        }
    }


}
