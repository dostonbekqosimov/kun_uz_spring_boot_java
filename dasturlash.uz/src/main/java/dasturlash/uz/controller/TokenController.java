package dasturlash.uz.controller;

import dasturlash.uz.dtos.JwtDTO;
import dasturlash.uz.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
public class TokenController {

    @GetMapping({"/generate"})
    public ResponseEntity<String> generateToken() {
        String jwt = JwtUtil.encode("mazgiyev", "ROLE_ADMIN");
        return ResponseEntity.ok(jwt);
    }

    @GetMapping({"/parse/{token}"})
    public ResponseEntity<JwtDTO> parseToken(@PathVariable("token") String token) {
        JwtDTO jwtDto = JwtUtil.decode(token);
        return ResponseEntity.ok(jwtDto);
    }
}