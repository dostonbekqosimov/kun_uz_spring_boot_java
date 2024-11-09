package dasturlash.uz.entity.article;

import dasturlash.uz.entity.Attach;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ArticleAttach {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "article_id")
    private String articleId;

    @Column(name = "attach_id")
    private String attachId;

    @ManyToOne
    @JoinColumn(name = "article_id", insertable = false, updatable = false)
    private Article post;

    @ManyToOne
    @JoinColumn(name = "attach_id", insertable = false, updatable = false)
    private Attach attach;
}
