package dasturlash.uz.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ArticleFullInfoDTO {
    private String id;
    private String title;
    private String description;
    private ImageInfo image;
    private LocalDateTime publishedDate;
}
