package dasturlash.uz.repository.customInterfaces;

import java.time.LocalDateTime;

// Bu ishlamedi chunki biz image uchun alohida table ochganmiz; [done]
public interface ArticleShortInfoMapper {

    String getId();

    String getTitle();

    String getDescription();

    String getImageId();

    LocalDateTime getPublishedDate();

}
