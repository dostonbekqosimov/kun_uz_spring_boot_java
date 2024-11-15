package dasturlash.uz.repository.customInterfaces;

import dasturlash.uz.dtos.ProfileShortInfoDTO;

import java.time.LocalDateTime;

public interface CommentMapper {
    String getId();
    String getReplyId();
    String getContent();
    LocalDateTime getCreatedDate();
    LocalDateTime getUpdatedDate();
    ProfileShortInfoDTO getProfile();
}
