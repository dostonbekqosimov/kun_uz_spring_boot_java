package dasturlash.uz.entity.article;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "article_share_record")
public class ArticleShareRecord {




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



        public ArticleShareRecord(String articleId, String ipAddress) {
            this.articleId = articleId;
            this.ipAddress = ipAddress;


        }
    }



