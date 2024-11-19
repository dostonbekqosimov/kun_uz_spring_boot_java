package dasturlash.uz.service.auth;

import dasturlash.uz.config.security.CustomUserDetails;
import dasturlash.uz.dtos.token.JwtDTO;
import dasturlash.uz.dtos.token.TokenDTO;
import dasturlash.uz.dtos.auth.JwtResponseDTO;
import dasturlash.uz.dtos.profile.ProfileResponseDTO;
import dasturlash.uz.dtos.auth.RegistrationDTO;
import dasturlash.uz.entity.Profile;
import dasturlash.uz.enums.LanguageEnum;
import dasturlash.uz.enums.Role;
import dasturlash.uz.enums.Status;
import dasturlash.uz.exceptions.AppBadRequestException;
import dasturlash.uz.exceptions.DataExistsException;
import dasturlash.uz.exceptions.UnauthorizedException;
import dasturlash.uz.repository.ProfileRepository;
import dasturlash.uz.service.AttachService;
import dasturlash.uz.service.ResourceBundleService;
import dasturlash.uz.util.JwtUtil;
import dasturlash.uz.util.LoginIdentifierService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final ProfileRepository profileRepository;
    private final EmailAuthService emailAuthService;
    private final SmsAuthService smsAuthService;
    private final AuthenticationManager authenticationManager;
    private final LoginIdentifierService loginIdentifierService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AttachService attachService;
    private final ResourceBundleService resourceBundleService;

    public String registration(RegistrationDTO dto, LanguageEnum lang) {
        String login = dto.getLogin();
        boolean isEmail = login.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$");
        boolean isPhoneNumber = login.matches("^\\+?[0-9]{10,15}$");

        if (isEmail) {
            existsByEmailOrPhone(login, null, lang);
        } else if (isPhoneNumber) {
            existsByEmailOrPhone(null, login, lang);
        } else {
            throw new IllegalArgumentException(resourceBundleService.getMessage("invalid.login.format", lang));
        }

        Profile profile = new Profile();
        profile.setName(dto.getName());
        profile.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        profile.setSurname(dto.getSurname());
        profile.setCreatedAt(LocalDateTime.now());
        profile.setRole(Role.ROLE_USER);
        profile.setLogin(login);
        profile.setVisible(Boolean.TRUE);
        profile.setStatus(Status.IN_REGISTRATION);


        return isEmail ?
                emailAuthService.registerViaEmail(dto, profile, lang) :
                smsAuthService.registerViaSms(dto, profile, lang);
    }

    public String registrationConfirm(Long id, LanguageEnum lang) {
        return emailAuthService.confirmEmail(id, lang);
    }

    public String registrationConfirmViaSms(String phone, String code, LanguageEnum lang) {
        return smsAuthService.confirmSms(phone, code, lang);
    }

    public String resendConfirmationEmail(Long id, LanguageEnum lang) {
        return emailAuthService.resendEmailConfirmation(id, lang);
    }

    public String resendConfirmationSms(String phone, LanguageEnum lang) {
        return smsAuthService.resendSmsVerification(phone, lang);
    }


    public JwtResponseDTO login(String login, String password, LanguageEnum lang) {


        Profile entity = profileRepository.findByLoginAndVisibleTrue(login)
                .orElseThrow(() -> new UnauthorizedException(resourceBundleService.getMessage("login.password.wrong", lang)));


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

                ProfileResponseDTO responseDTO = new ProfileResponseDTO();
                responseDTO.setName(entity.getName());
                responseDTO.setSurname(entity.getSurname());
                responseDTO.setLogin(entity.getLogin());
                responseDTO.setRole(entity.getRole());
                responseDTO.setAccessToken(JwtUtil.encode(login, userDetails.getRole().toString()));
                responseDTO.setRefreshToken(JwtUtil.refreshToken(login, userDetails.getRole().toString()));
                responseDTO.setPhoto(attachService.getDto(entity.getPhotoId()));


                return response;
            }
            throw new UnauthorizedException(resourceBundleService.getMessage("login.password.wrong", lang));
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException(resourceBundleService.getMessage("login.password.wrong", lang));

        }
    }

    public TokenDTO getNewAccessToken(TokenDTO dto, LanguageEnum lang) {
        // First check if refresh token is provided
        if (dto.getRefreshToken() == null || dto.getRefreshToken().trim().isEmpty()) {
            throw new AppBadRequestException(resourceBundleService.getMessage("refresh.token.required", lang));
        }

        // Validate the refresh token
        JwtUtil.TokenValidationResult validationResult = JwtUtil.validateToken(dto.getRefreshToken());
        if (!validationResult.isValid()) {
            throw new UnauthorizedException(validationResult.getMessage());
        }

        try {
            JwtDTO jwtDTO = JwtUtil.decode(dto.getRefreshToken());


            // Bu yaxshi yechim ekan, keyin email yoki phone ekanligini client taraf anqilarkan
            Profile profile = loginIdentifierService.identifyInputType(jwtDTO.getLogin(), lang);

//            Profile profile = profileRepository.findByPhoneOrEmail(jwtDTO.getLogin()).get();


            // Check if user is still active
            if (!profile.getStatus().equals(Status.ACTIVE)) {
                throw new UnauthorizedException(resourceBundleService.getMessage("account.not.active", lang));
            }

            TokenDTO response = new TokenDTO();
            response.setAccessToken(JwtUtil.encode(profile.getPhone(), profile.getRole().name()));
            response.setRefreshToken(JwtUtil.refreshToken(profile.getPhone(), profile.getRole().name()));
            return response;

        } catch (JwtException e) {
            throw new UnauthorizedException(resourceBundleService.getMessage("refresh.token.invalid", lang));
        }
    }




    private void existsByEmailOrPhone(String email, String phone, LanguageEnum lang) {

        if (email != null && !email.trim().isEmpty()) {
            boolean isEmailExist = profileRepository.existsByEmailAndVisibleTrue(email);
            if (isEmailExist) {
                throw new DataExistsException(resourceBundleService.getMessage("email.exists", lang));
            }
        }

        if (phone != null && !phone.trim().isEmpty()) {
            boolean isPhoneExist = profileRepository.existsByPhoneAndVisibleTrue(phone);
            if (isPhoneExist) {
                throw new DataExistsException(resourceBundleService.getMessage("phone.exists", lang));
            }
        }
    }

}
