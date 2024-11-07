package dasturlash.uz.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ArticleUpdateRequestDTO {

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must be less than 255 characters")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Content is required")
    private String content;

    private int sharedCount;

    @NotNull(message = "Image ID is required")
    private String imageId;

    @NotNull(message = "Region ID is required")
    private String regionId;

    @NotNull(message = "Category ID is required")
    private String categoryId;
}
