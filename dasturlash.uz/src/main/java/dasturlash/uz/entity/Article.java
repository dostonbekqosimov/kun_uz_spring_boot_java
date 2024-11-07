package dasturlash.uz.entity;

import dasturlash.uz.enums.ArticleStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "articles")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title;
    private String description;
    private String content;
    private Integer sharedCount;
    private Boolean visible;
    private Integer viewCount;
    private LocalDateTime createdDate;
    private LocalDateTime publishedDate;

    @Enumerated(EnumType.STRING)
    private ArticleStatus status;

    @ManyToOne
    @JoinColumn(name = "image_id", insertable = false, updatable = false)
    private Attach image;

    @Column(name = "image_id")
    private String imageId;

    @ManyToOne
    @JoinColumn(name = "region_id", insertable = false, updatable = false)
    private Region region;

    @Column(name = "region_id")
    private Integer regionId;

    @ManyToOne
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private Category category;

    @Column(name = "category_id")
    private Integer categoryId;

    @ManyToOne
    @JoinColumn(name = "moderator_id", insertable = false, updatable = false)
    private Profile moderator;

    @Column(name = "moderator_id")
    private Integer moderatorId;

    @ManyToOne
    @JoinColumn(name = "publisher_id", insertable = false, updatable = false)
    private Profile publisher;

    @Column(name = "publisher_id")
    private Integer publisherId;
}
