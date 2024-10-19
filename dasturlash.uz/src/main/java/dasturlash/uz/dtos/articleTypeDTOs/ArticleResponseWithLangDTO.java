package dasturlash.uz.dtos.articleTypeDTOs;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ArticleResponseWithLangDTO {

    private Long id;
    private Integer orderNumber;
    private String name;
    private Boolean visible;
    private LocalDateTime createdDate;

}
