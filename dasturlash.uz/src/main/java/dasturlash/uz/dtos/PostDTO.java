package dasturlash.uz.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostDTO {

    private Integer id;
    private String title;
    private String content;
    private List<AttachDTO> attachDTOList;

}
