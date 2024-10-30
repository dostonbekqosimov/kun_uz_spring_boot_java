package dasturlash.uz.service;

import dasturlash.uz.dtos.profileDTOs.ProfileResponseDTO;
import dasturlash.uz.dtos.profileDTOs.RegistrationDTO;
import dasturlash.uz.entity.Profile;
import dasturlash.uz.enums.Role;
import dasturlash.uz.enums.Status;
import dasturlash.uz.exceptions.DataExistsException;
import dasturlash.uz.exceptions.DataNotFoundException;
import dasturlash.uz.repository.ProfileRepository;
import dasturlash.uz.util.JwtUtil;
import dasturlash.uz.util.MD5Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final ProfileRepository profileRepository;
    private final EmailAuthService emailAuthService;
    private final SmsAuthService smsAuthService;

    public String registration(RegistrationDTO dto) {
        String login = dto.getLogin();
        boolean isEmail = login.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$");
        boolean isPhoneNumber = login.matches("^\\+?[0-9]{10,15}$");

        if (isEmail) {
            existsByEmailOrPhone(login, null);
        } else if (isPhoneNumber) {
            existsByEmailOrPhone(null, login);
        } else {
            throw new IllegalArgumentException("Invalid login format. Please provide a valid email or phone number.");
        }

        Profile profile = new Profile();
        profile.setName(dto.getName());
        profile.setPassword(MD5Util.getMd5(dto.getPassword()));
        profile.setSurname(dto.getSurname());
        profile.setCreatedAt(LocalDateTime.now());
        profile.setRole(Role.ROLE_USER);
        profile.setVisible(Boolean.TRUE);
        profile.setStatus(Status.IN_REGISTRATION);


        return isEmail ?
                emailAuthService.registerViaEmail(dto, profile) :
                smsAuthService.registerViaSms(dto, profile);
    }
    public String registrationConfirm(Long id) {
        return emailAuthService.confirmEmail(id);
    }

    public String registrationConfirmViaSms(String phone, String code) {
        return smsAuthService.confirmSms(phone, code);
    }

    public String resendConfirmationEmail(Long id) {
        return emailAuthService.resendEmailConfirmation(id);
    }

    public String resendConfirmationSms(String phone) {
        return smsAuthService.resendSmsVerification(phone);
    }


    public ProfileResponseDTO login(String login, String password) {

        boolean isEmail = login.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$");
        boolean isPhoneNumber = login.matches("^\\+?[0-9]{10,15}$");

        if (isEmail) {
//            existsByEmailOrPhone(login, null);
           return loginByEmail(login, password);
        } else if (isPhoneNumber) {
//            existsByEmailOrPhone(null, login);
            return loginByPhone(login, password);
        } else {
            throw new IllegalArgumentException("Invalid login format. Please provide a valid email or phone number.");
        }

    }

    private ProfileResponseDTO loginByPhone(String login, String password) {

        // Find the user by email
        Optional<Profile> optional = profileRepository.findByPhoneAndVisibleTrue(login);
        if (optional.isEmpty()) {
            throw new DataNotFoundException("Email or password is wrong");
        }

        Profile entity = optional.get();

        // Check if the password matches
        if (!entity.getPassword().equals(MD5Util.getMd5(password))) {

            throw new DataNotFoundException("Email or password is wrong");
        }

        // Check if the profile is active
        if (!entity.getStatus().equals(Status.ACTIVE)) {
            throw new DataNotFoundException("Account is not active");
        }

        ProfileResponseDTO dto = new ProfileResponseDTO();
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setEmail(entity.getEmail());
        dto.setRole(entity.getRole());
        dto.setJwtToken(JwtUtil.encode(entity.getPhone(), entity.getRole().toString()));
        return dto;
    }

    private ProfileResponseDTO loginByEmail(String login, String password) {

        // Find the user by email
        Optional<Profile> optional = profileRepository.findByEmailAndVisibleTrue(login);
        if (optional.isEmpty()) {
            throw new DataNotFoundException("Email or password is wrong");

        }

        Profile entity = optional.get();

        // Check if the password matches
        if (!entity.getPassword().equals(MD5Util.getMd5(password))) {
            throw new DataNotFoundException("Email or password is wrong");

        }

        // Check if the profile is active
        if (!entity.getStatus().equals(Status.ACTIVE)) {
            throw new DataNotFoundException("Account is not active");
        }

        ProfileResponseDTO dto = new ProfileResponseDTO();
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setEmail(entity.getEmail());
        dto.setRole(entity.getRole());
        dto.setJwtToken(JwtUtil.encode(entity.getEmail(), entity.getRole().toString()));
        return dto;
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
