package dasturlash.uz.dtos;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ArticleShortInfoDTO {
    private String id;
    private String title;
    private String description;
    private String content;
    private int sharedCount;
    private RegionInfo region;
    private CategoryInfo category;
    private LocalDateTime publishedDate;
    private int viewCount;
    private int likeCount;
    private List<TagInfo> tagList;

}
