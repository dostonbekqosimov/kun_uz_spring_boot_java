package dasturlash.uz.dtos;

import dasturlash.uz.enums.ArticleStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ArticleFilterDTO {

    private String id;
    private String title;
    private Long regionId;
    private Long categoryId;
    private LocalDateTime createdDateFrom;
    private LocalDateTime createdDateTo;
    private LocalDateTime publishedDateFrom;
    private LocalDateTime publishedDateTo;
    private Long moderatorId;
    private Long publisherId;
    private ArticleStatus status;

}
