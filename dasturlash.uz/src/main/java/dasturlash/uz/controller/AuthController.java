package dasturlash.uz.controller;

import dasturlash.uz.dtos.token.TokenDTO;
import dasturlash.uz.dtos.token.TokenRefreshRequestDTO;
import dasturlash.uz.dtos.auth.JwtResponseDTO;
import dasturlash.uz.dtos.auth.LoginDTO;
import dasturlash.uz.dtos.auth.RegistrationDTO;
import dasturlash.uz.enums.LanguageEnum;
import dasturlash.uz.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;


    @PostMapping("/registration")
    public ResponseEntity<String> registration(@RequestBody @Valid RegistrationDTO dto,
                                               @RequestHeader(value = "Accept-Language", defaultValue = "uz") LanguageEnum lang) {

        return ResponseEntity.ok(authService.registration(dto, lang));
    }

    @GetMapping("/registration/confirm/{id}")
    public ResponseEntity<String> registration(@PathVariable Long id,
                                               @RequestHeader(value = "Accept-Language", defaultValue = "uz") LanguageEnum lang) {

        return ResponseEntity.ok(authService.registrationConfirm(id,  lang));
    }

    @GetMapping("/registration/resend/{id}")
    public ResponseEntity<String> resend(@PathVariable Long id,
                                         @RequestHeader(value = "Accept-Language", defaultValue = "uz") LanguageEnum lang) {
        return ResponseEntity.ok(authService.resendConfirmationEmail(id, lang));
    }

    @PostMapping("/registration/resend/{id}")
    public ResponseEntity<String> resendConfirmation(@PathVariable Long id,
                                                     @RequestHeader(value = "Accept-Language", defaultValue = "uz") LanguageEnum lang) {
        return ResponseEntity.ok(authService.resendConfirmationEmail(id, lang));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody @Valid LoginDTO loginDTO,
                                                @RequestHeader(value = "Accept-Language", defaultValue = "uz") LanguageEnum lang) {
        return ResponseEntity.ok(authService.login(loginDTO.getLogin(), loginDTO.getPassword(), lang));
    }

    @PostMapping("/registration/confirm/sms")
    public ResponseEntity<String> confirmSmsRegistration(
            @RequestParam String phone,
            @RequestParam String code) {
        String result = authService.registrationConfirmViaSms(phone, code);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/registration/resend/sms")
    public ResponseEntity<String> resendSmsVerification(
            @RequestParam String phone) {
        String result = authService.resendConfirmationSms(phone);
        return ResponseEntity.ok(result);
    }


    @PostMapping("/refresh")
    public ResponseEntity<TokenDTO> refreshToken(@Valid @RequestBody TokenRefreshRequestDTO request,
                                                 @RequestHeader(value = "Accept-Language", defaultValue = "uz") LanguageEnum lang) {
        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setRefreshToken(request.getRefreshToken());
        return ResponseEntity.ok(authService.getNewAccessToken(tokenDTO, lang));
    }

}
