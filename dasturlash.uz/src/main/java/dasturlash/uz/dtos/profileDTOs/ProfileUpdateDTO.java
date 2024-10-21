package dasturlash.uz.dtos.profileDTOs;

import lombok.Data;

@Data
public class ProfileUpdateDTO {
    private String name;
    private String surname;
    private String email;
    private String phone;
    private String status;

}
