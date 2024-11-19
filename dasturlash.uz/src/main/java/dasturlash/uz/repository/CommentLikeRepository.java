package dasturlash.uz.repository;

import dasturlash.uz.entity.CommentLike;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, String> {

    Optional<CommentLike> findByProfileIdAndCommentId(Long profileId, String commentId);



    @Query("UPDATE CommentLike a SET a.visible = :visible WHERE a.profileId = :profileId AND a.commentId = :commentId")
    @Modifying
    @Transactional
    Integer changeVisibility(String commentId, boolean visible, Long profileId);



}
