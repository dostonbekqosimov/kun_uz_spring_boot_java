package dasturlash.uz.repository.customInterfaces;

import java.time.LocalDateTime;
import java.util.List;

public interface ArticleFullInfoMapper {

    String getId();

    String getTitle();

    String getDescription();

    String getContent();

    Integer getSharedCount();

    Integer getViewCount();

    Integer getLikeCount();

    LocalDateTime getPublishedDate();

    // Nested interface for Region projection
    RegionInfo getRegion();

    interface RegionInfo {
        Long getId();
        String getName();
    }

    // Nested interface for Category projection
    CategoryInfo getCategory();

    interface CategoryInfo {
        Long getId();
        String getName();
    }

    // Nested interface for Tag projection
    List<String > getTagList();

    interface TagInfo {
        String getName();
    }
}
