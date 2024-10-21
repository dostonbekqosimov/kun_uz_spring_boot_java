package dasturlash.uz.entity;


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
    private String status;

    @ManyToOne
    @JoinColumn(name = "image_id")
    private Attach image;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;


    @ManyToOne
    @JoinColumn(name = "moderator_id")
    private Profile moderator;

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private Profile publisher;


}
