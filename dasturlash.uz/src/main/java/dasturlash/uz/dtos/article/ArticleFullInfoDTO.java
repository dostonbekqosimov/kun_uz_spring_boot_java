package dasturlash.uz.dtos.article;

import dasturlash.uz.dtos.AttachDTO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ArticleFullInfoDTO {

    private String id;
    private String title;
    private String description;
    private String content;
    private Integer sharedCount;
}
