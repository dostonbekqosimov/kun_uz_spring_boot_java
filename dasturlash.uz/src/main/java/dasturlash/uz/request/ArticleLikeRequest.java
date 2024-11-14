package dasturlash.uz.request;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleLikeRequest {

    private Long profileId;
    private String articleId;

}
