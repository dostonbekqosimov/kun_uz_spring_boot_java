package dasturlash.uz.dtos.profileDTOs;

import com.fasterxml.jackson.annotation.JsonInclude;
import dasturlash.uz.entity.Attach;
import dasturlash.uz.enums.Role;
import dasturlash.uz.enums.Status;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    private String password;
    private Status status;
    private Boolean visible;
    private Role role;
    private LocalDateTime createdAt;
    private Attach photo;


}
