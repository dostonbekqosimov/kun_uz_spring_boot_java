package dasturlash.uz.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentResponseDTO {

    private String id;
    private String content;
    private String articleId;
    private String replyId;
    private ProfileShortInfoDTO userInfo;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

}
