package dasturlash.uz.controller;

import dasturlash.uz.dtos.profileDTOs.LoginDTO;
import dasturlash.uz.dtos.profileDTOs.RegistrationDTO;
import dasturlash.uz.service.AuthService;
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
    public ResponseEntity<String> registration(@RequestBody RegistrationDTO dto){
        return ResponseEntity.ok(authService.registration(dto));
    }

    @GetMapping("/registration/confirm/{id}")
    public ResponseEntity<String> registration(@PathVariable Long id){
        return ResponseEntity.ok(authService.registrationConfirm(id));
    }

    @GetMapping("/registration/resend/{id}")
    public ResponseEntity<String> resend(@PathVariable Long id){
        return ResponseEntity.ok(authService.resendConfirmationEmail(id));
    }

    @PostMapping("/registration/resend/{id}")
    public ResponseEntity<String> resendConfirmation(@PathVariable Long id) {
        return ResponseEntity.ok(authService.resendConfirmationEmail(id));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {
        return ResponseEntity.ok(authService.login(loginDTO.getEmail(), loginDTO.getPassword()));
    }



}
