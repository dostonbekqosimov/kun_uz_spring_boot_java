package dasturlash.uz.dtos.article;

import dasturlash.uz.dtos.AttachDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ArticleRequestDTO {

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must be less than 255 characters")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Content is required")
    private String content;

    // Buni qo'shyabman chunki rasmlarni o'zi kunuzdan olinarkan yani men o'zi db da bor rasmlardan foydalanaman ekan
    private String imageId;

    private List<AttachDTO> attachDTOList;

    @NotNull(message = "Region ID is required")
    private Long regionId;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    @NotNull(message = "Article types are required")
    private List<Long> articleTypeList = new ArrayList<>();
}
