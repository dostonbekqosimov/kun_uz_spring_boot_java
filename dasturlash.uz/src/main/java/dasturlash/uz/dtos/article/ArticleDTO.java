package dasturlash.uz.dtos.article;

import com.fasterxml.jackson.annotation.JsonInclude;
import dasturlash.uz.dtos.AttachDTO;
import dasturlash.uz.dtos.articleType.ArticleTypeResponseDTO;
import dasturlash.uz.entity.article.ArticleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleDTO {

    private String id;

    private String title;

    private String description;

    private String content;

    private String imageId;

    private Long regionId;

    private Long categoryId;

    private Long moderatorId;

    private LocalDateTime createdDate;


    private List<ArticleTypeResponseDTO> articleTypeList;

    private List<AttachDTO> imageList;

}
