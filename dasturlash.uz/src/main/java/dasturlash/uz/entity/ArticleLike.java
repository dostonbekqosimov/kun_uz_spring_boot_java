package dasturlash.uz.entity;


import dasturlash.uz.entity.article.Article;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "article_likes")
public class ArticleLike {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "profile_id")
    private Long profileId;
    @ManyToOne
    @JoinColumn(name = "profile_id", updatable = false, insertable = false)
    private Profile profile;

    @Column(name = "article_id")
    private String articleId;
    @ManyToOne
    @JoinColumn(name = "article_id", updatable = false, insertable = false)
    private Article article;

    private LocalDateTime createdDate;

    private Boolean status;


}
