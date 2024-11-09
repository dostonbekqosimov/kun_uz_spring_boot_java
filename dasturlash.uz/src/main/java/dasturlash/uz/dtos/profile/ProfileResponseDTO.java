package dasturlash.uz.dtos.profile;

import com.fasterxml.jackson.annotation.JsonInclude;
import dasturlash.uz.dtos.AttachDTO;
import dasturlash.uz.enums.Role;
import dasturlash.uz.enums.Status;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileResponseDTO {

    private Long id;
    private String name;
    private String surname;
    private String email;
    private String phone;
    private String login;
    private String password;
    private Status status;
    private Boolean visible;
    private Role role;
    private LocalDateTime createdAt;
    private AttachDTO photo;

    private String accessToken;
    private String refreshToken;



}
