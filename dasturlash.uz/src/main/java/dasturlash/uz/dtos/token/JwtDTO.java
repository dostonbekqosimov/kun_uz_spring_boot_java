package dasturlash.uz.dtos.token;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class JwtDTO {

    private String login;
    private String role;
}
