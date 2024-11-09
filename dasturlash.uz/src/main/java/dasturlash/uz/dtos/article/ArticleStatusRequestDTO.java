package dasturlash.uz.dtos.article;

import dasturlash.uz.enums.ArticleStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleStatusRequestDTO {

    private ArticleStatus status;
}
