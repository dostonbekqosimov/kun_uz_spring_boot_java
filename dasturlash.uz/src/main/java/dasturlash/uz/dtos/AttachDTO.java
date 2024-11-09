package dasturlash.uz.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttachDTO {

    private String id;
    private String originName;
    private Long size;
    private String extension;
    private LocalDateTime createdData;
    private String url;


}
