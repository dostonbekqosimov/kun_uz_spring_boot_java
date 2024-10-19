package dasturlash.uz.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "categories")
public class Category {

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

