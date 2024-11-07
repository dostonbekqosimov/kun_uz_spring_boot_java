package dasturlash.uz.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleStatusRequestDTO {


    @NotBlank(message = "Status is required")
    private String status;
}
