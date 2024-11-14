package dasturlash.uz.entity.article;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "article_view_record")
public class ArticleViewRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String  id;

    @Column(name = "article_id")
    private String articleId;

    @ManyToOne
    @JoinColumn(name = "article_id", updatable = false, insertable = false)
    private Article article;

    private String ipAddress;
    private LocalDateTime createdDate;



    public ArticleViewRecord(String articleId, String ipAddress) {
        this.articleId = articleId;
        this.ipAddress = ipAddress;


    }
}
