package dasturlash.uz.dtos.articleTypeDTOs;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleTypeResponseDTO {
    private Long id;
    private Integer orderNumber;
    private String nameUz;
    private String nameRu;
    private String nameEn;
    private Boolean visible;
    private LocalDateTime createdDate;


}
