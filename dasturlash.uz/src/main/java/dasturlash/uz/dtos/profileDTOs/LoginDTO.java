package dasturlash.uz.dtos.profileDTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {

    @NotBlank(message = "Email is required")
    private String login;

    @NotBlank(message = "Password is required")
    private String password;

    // Getters and setters
}

