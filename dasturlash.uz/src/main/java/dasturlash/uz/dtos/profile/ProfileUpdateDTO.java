package dasturlash.uz.dtos.profile;

import dasturlash.uz.enums.Role;
import dasturlash.uz.enums.Status;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProfileUpdateDTO {


    @NotBlank(message = "Name cannot be empty")
    @Size(max = 50, message = "Name cannot be longer than 50 characters")
    private String name;

    @NotBlank(message = "Surname cannot be empty")
    @Size(max = 50, message = "Surname cannot be longer than 50 characters")
    private String surname;


    @Email(message = "Email should be valid")
    private String email;


    @Pattern(regexp = "\\+?[0-9. ()-]{7,25}", message = "Phone number is invalid")
    private String phone;

    private Status status;

    private Role role;

    @AssertTrue(message = "Either email or phone must be provided")
    public boolean isEmailOrPhoneProvided() {
        return (email != null && !email.isBlank()) ||
               (phone != null && !phone.isBlank());
    }

}
