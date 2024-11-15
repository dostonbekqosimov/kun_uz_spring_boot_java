package dasturlash.uz.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDTO {

    @NotBlank
    private String content;

    private String articleId;

    private String replyId;
}
