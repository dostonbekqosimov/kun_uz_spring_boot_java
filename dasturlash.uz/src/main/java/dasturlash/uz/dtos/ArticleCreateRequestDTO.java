package dasturlash.uz.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ArticleCreateRequestDTO {

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must be less than 255 characters")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Content is required")
    private String content;

    @NotNull(message = "Image ID is required")
    private String imageId;

    @NotNull(message = "Region ID is required")
    private Integer regionId;

    @NotNull(message = "Category ID is required")
    private Integer categoryId;

    @NotNull(message = "Article types are required")
    private List<String> articleTypes;
}
