package dasturlash.uz.dtos.profileDTOs;

import lombok.Data;

@Data
public class ProfileCreationDTO {

    private String name;
    private String surname;
    private String email;
    private String phone;
    private String password;
    private String status;
    private String role;
}
