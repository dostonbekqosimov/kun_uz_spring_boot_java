package dasturlash.uz.dtos.articleType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleTypeRequestDTO {

    @NotNull(message = "orderNumber cannot be null")
    private Integer orderNumber;
    @NotBlank(message = "nameUz cannot be empty")
    @Size(max = 50, message = "Name cannot be longer than 50 characters")
    private String nameUz;
    @NotBlank(message = "nameRu cannot be empty")
    @Size(max = 50, message = "Name cannot be longer than 50 characters")
    private String nameRu;
    @NotBlank(message = "nameEn cannot be empty")
    @Size(max = 50, message = "Name cannot be longer than 50 characters")
    private String nameEn;
}
