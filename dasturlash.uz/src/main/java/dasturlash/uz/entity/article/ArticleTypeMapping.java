package dasturlash.uz.entity.article;



import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "article_types_mapping")
public class ArticleTypeMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "article_id")
    private String articleId;

    @Column(name = "article_type_id")
    private Long articleTypeId;

    @ManyToOne
    @JoinColumn(name = "article_id", insertable = false, updatable = false)
    private Article article;

    @ManyToOne
    @JoinColumn(name = "article_type_id", insertable = false, updatable = false)
    private ArticleType articleType;

    private LocalDateTime createdDate;
}
