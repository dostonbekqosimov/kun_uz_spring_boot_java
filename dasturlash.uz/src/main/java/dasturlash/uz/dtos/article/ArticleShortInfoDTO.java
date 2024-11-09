package dasturlash.uz.dtos.article;

import dasturlash.uz.dtos.AttachDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ArticleShortInfoDTO {
    private String id;
    private String title;
    private String description;
    private AttachDTO image;
    private LocalDateTime publishedDate;


}
