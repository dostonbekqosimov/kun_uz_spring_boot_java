package dasturlash.uz.service.auth;

import dasturlash.uz.config.CustomUserDetails;
import dasturlash.uz.dtos.JwtDTO;
import dasturlash.uz.dtos.TokenDTO;
import dasturlash.uz.dtos.profileDTOs.JwtResponseDTO;
import dasturlash.uz.dtos.profileDTOs.ProfileResponseDTO;
import dasturlash.uz.dtos.profileDTOs.RegistrationDTO;
import dasturlash.uz.entity.Profile;
import dasturlash.uz.enums.Role;
import dasturlash.uz.enums.Status;
import dasturlash.uz.exceptions.AppBadRequestException;
import dasturlash.uz.exceptions.DataExistsException;
import dasturlash.uz.exceptions.DataNotFoundException;
import dasturlash.uz.exceptions.UnauthorizedException;
import dasturlash.uz.repository.ProfileRepository;
import dasturlash.uz.util.JwtUtil;
import dasturlash.uz.util.LoginIdentifierService;
import dasturlash.uz.util.MD5Util;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final ProfileRepository profileRepository;
    private final EmailAuthService emailAuthService;
    private final SmsAuthService smsAuthService;
    private final AuthenticationManager authenticationManager;
    private final LoginIdentifierService loginIdentifierService;

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


    public JwtResponseDTO login(String login, String password) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(login, password)
            );

            if (authentication.isAuthenticated()) {
                CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

                JwtResponseDTO response = new JwtResponseDTO();
                response.setLogin(login);
                response.setToken(JwtUtil.encode(login, userDetails.getRole().toString()));
                response.setRefreshToken(JwtUtil.refreshToken(login, userDetails.getRole().toString()));
                response.setRoles(List.of(userDetails.getRole().toString()));

                return response;
            }
            throw new UnauthorizedException("Login or password is wrong");
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException("Login or password is wrong");

        }
    }

    public TokenDTO getNewAccessToken(TokenDTO dto) {
        // First check if refresh token is provided
        if (dto.getRefreshToken() == null || dto.getRefreshToken().trim().isEmpty()) {
            throw new AppBadRequestException("Refresh token is required");
        }

        // Validate the refresh token
        JwtUtil.TokenValidationResult validationResult = JwtUtil.validateToken(dto.getRefreshToken());
        if (!validationResult.isValid()) {
            throw new UnauthorizedException(validationResult.getMessage());
        }

        try {
            JwtDTO jwtDTO = JwtUtil.decode(dto.getRefreshToken());


            Profile profile = loginIdentifierService.identifyInputType(jwtDTO.getLogin());


            // Check if user is still active
            if (!profile.getStatus().equals(Status.ACTIVE)) {
                throw new UnauthorizedException("User account is not active");
            }

            TokenDTO response = new TokenDTO();
            response.setAccessToken(JwtUtil.encode(profile.getPhone(), profile.getRole().name()));
            response.setRefreshToken(JwtUtil.refreshToken(profile.getPhone(), profile.getRole().name()));
            return response;

        } catch (JwtException e) {
            throw new UnauthorizedException("Invalid refresh token");
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
        dto.setAccessToken(JwtUtil.encode(entity.getPhone(), entity.getRole().toString()));
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
        dto.setAccessToken(JwtUtil.encode(entity.getEmail(), entity.getRole().toString()));
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
