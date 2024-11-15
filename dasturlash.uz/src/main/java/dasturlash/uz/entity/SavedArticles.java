package dasturlash.uz.entity;

import dasturlash.uz.entity.article.Article;
import dasturlash.uz.enums.ReactionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "saved_articles")
@Getter
@Setter
public class SavedArticles {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Enumerated(EnumType.STRING)
    private ReactionType type;

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

    private Boolean visible;

    private LocalDateTime createdDate;
}
