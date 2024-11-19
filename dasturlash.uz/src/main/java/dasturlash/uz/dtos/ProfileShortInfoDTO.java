package dasturlash.uz.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileShortInfoDTO {

    private Long profileId;

    private String name;

    private String surname;
}
