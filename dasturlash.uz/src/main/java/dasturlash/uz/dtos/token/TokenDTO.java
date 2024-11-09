package dasturlash.uz.dtos.token;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenDTO {
    private String accessToken;
    private String refreshToken;
}
