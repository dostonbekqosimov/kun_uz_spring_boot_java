package dasturlash.uz.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "article_types")
public class ArticleType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer orderNumber;

    private String nameUz;
    private String nameRu;
    private String nameEn;

    private Boolean visible;
    private LocalDateTime createdDate;


}
