package dasturlash.uz.entity.article;

import dasturlash.uz.entity.Tag;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "article_tag_mapping")
public class ArticleTag {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", insertable = false, updatable = false)
    private Article article;

    @Column(name = "article_id")
    private String articleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", updatable = false, insertable = false)
    private Tag tag;

    @Column(name = "tag_id")
    private Long tagId;

    @Column(name = "visible")
    private Boolean visible;

    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;
}
