package dasturlash.uz.entity.article;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "article_tags")
public class ArticleTag {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", insertable = false, updatable = false)
    private Article article;

    @Column(name = "article_id")
    private String articleId;


    private Boolean visible;

    @Column(name = "created_date")
    private LocalDateTime createdDate;




}
