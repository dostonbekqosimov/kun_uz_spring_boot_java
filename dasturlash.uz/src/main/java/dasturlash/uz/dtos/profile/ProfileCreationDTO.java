package dasturlash.uz.dtos.profile;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProfileCreationDTO {


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

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    // Role and Status can be empty for the time being.
    // @NotBlank(message = "Status cannot be empty")
    private String status;

    //    @NotBlank(message = "Role cannot be empty")
    private String role;

    @AssertTrue(message = "Either email or phone must be provided")
    public boolean isEmailOrPhoneProvided() {
        return (email != null && !email.isBlank()) ||
               (phone != null && !phone.isBlank());
    }
}
