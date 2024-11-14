package dasturlash.uz.dtos.article;

import dasturlash.uz.dtos.AttachDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ArticleFullInfoDTO {

    private String id; // UUID
    private String title;
    private String description;
    private String content;
    private Integer sharedCount;
    private RegionDTO region;
    private CategoryDTO category;
    private LocalDateTime publishedDate;
    private Integer viewCount;
    private Integer likeCount;
    private List<String> tagList; // List of tag names
}
