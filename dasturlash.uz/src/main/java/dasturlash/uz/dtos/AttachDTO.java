package dasturlash.uz.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AttachDTO {

    private String id;
    private String originName;
    private Long size;
    private String extension;
    private LocalDateTime createdData;
    private String url;


}
