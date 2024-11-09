package dasturlash.uz.dtos.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JwtResponseDTO {
    private String token;
    private String type = "Bearer";
    private String refreshToken;
    private String login;
    private List<String> roles;
}
