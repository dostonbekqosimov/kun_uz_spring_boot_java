package dasturlash.uz.controller;

import dasturlash.uz.dtos.profileDTOs.LoginDTO;
import dasturlash.uz.dtos.profileDTOs.ProfileResponseDTO;
import dasturlash.uz.dtos.profileDTOs.RegistrationDTO;
import dasturlash.uz.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/registration")
    public ResponseEntity<String> registration(@RequestBody @Valid RegistrationDTO dto) {
        return ResponseEntity.ok(authService.registration(dto));
    }

    @GetMapping("/registration/confirm/{id}")
    public ResponseEntity<String> registration(@PathVariable Long id) {
        return ResponseEntity.ok(authService.registrationConfirm(id));
    }

    @GetMapping("/registration/resend/{id}")
    public ResponseEntity<String> resend(@PathVariable Long id) {
        return ResponseEntity.ok(authService.resendConfirmationEmail(id));
    }

    @PostMapping("/registration/resend/{id}")
    public ResponseEntity<String> resendConfirmation(@PathVariable Long id) {
        return ResponseEntity.ok(authService.resendConfirmationEmail(id));
    }

    @PostMapping("/login")
    public ResponseEntity<ProfileResponseDTO> login(@RequestBody @Valid LoginDTO loginDTO) {
        return ResponseEntity.ok(authService.login(loginDTO.getLogin(), loginDTO.getPassword()));
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


}
