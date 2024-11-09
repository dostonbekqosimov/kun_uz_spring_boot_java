package dasturlash.uz.entity.article;

import dasturlash.uz.entity.Attach;
import dasturlash.uz.entity.Category;
import dasturlash.uz.entity.Profile;
import dasturlash.uz.entity.Region;
import dasturlash.uz.enums.ArticleStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "articles")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String title;
    @Column(columnDefinition = "text")
    private String description;
    @Column(columnDefinition = "text")
    private String content;
    private Integer sharedCount;
    private Boolean visible;
    private Integer viewCount;
    private LocalDateTime createdDate;
    private LocalDateTime publishedDate;

    @Column(name = "image_id")
    private String imageId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id", updatable = false, insertable = false)
    private Attach image;

    @Enumerated(EnumType.STRING)
    private ArticleStatus status;

    @ManyToOne
    @JoinColumn(name = "region_id", insertable = false, updatable = false)
    private Region region;

    @Column(name = "region_id")
    private Long regionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private Category category;

    @Column(name = "category_id")
    private Long categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moderator_id", insertable = false, updatable = false)
    private Profile moderator;

    @Column(name = "moderator_id")
    private Long moderatorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id", insertable = false, updatable = false)
    private Profile publisher;

    @Column(name = "publisher_id")
    private Long publisherId;
}
