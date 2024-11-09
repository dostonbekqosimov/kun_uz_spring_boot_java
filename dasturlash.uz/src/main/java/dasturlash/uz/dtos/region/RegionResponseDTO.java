package dasturlash.uz.dtos.region;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RegionResponseDTO {


    private Long id;
    private Integer orderNumber;
    private String nameUz;
    private String nameRu;
    private String nameEn;
    private Boolean visible;
    private LocalDateTime createdDate;
}
