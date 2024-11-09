package dasturlash.uz.dtos.profile;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProfilePhotoUpdateDTO {

    @NotBlank(message = "photoId cannot be null")
    private UUID photoId;
}
