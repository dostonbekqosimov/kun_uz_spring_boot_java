package dasturlash.uz.repository;

import dasturlash.uz.entity.Comment;
import dasturlash.uz.repository.customInterfaces.CommentMapper;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, String> {
    Optional<Comment> findByIdAndVisibleTrue(String id);

    @Transactional
    @Modifying
    @Query("UPDATE Comment c SET c.visible = :visible WHERE c.id = :commentId")
    int updateVisibilityById(@Param("visible") boolean visible, @Param("commentId") String commentId);


    @Query("SELECT " +
            "   c.id AS id, " +
            "c.replyId as replyId, " +
            "c.content as content, " +
            "   c.createdDate AS createdDate, " +
            "   c.updatedDate AS updatedDate, " +
            "   NEW dasturlash.uz.dtos.ProfileShortInfoDTO(c.profileId, p.name, p.surname) AS profile " +
            "FROM Comment c " +
            "LEFT JOIN Profile p ON c.profileId = p.id " +
            "WHERE c.articleId = :articleId AND c.visible = true " )
    PageImpl<CommentMapper> findCommentListByArticleId(@Param("articleId") String articleId, Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE Article SET likeCount = COALESCE(sharedCount, 0) + 1 WHERE id = :articleId")
    void incrementLikeCount(String articleId);

    @Transactional
    @Modifying
    @Query("UPDATE Article SET likeCount = GREATEST(COALESCE(likeCount, 0) - 1, 0) WHERE id = :articleId")
    void decrementLikeCount(@Param("articleId") String articleId);


    @Transactional
    @Modifying
    @Query("UPDATE Article SET dislikeCount = COALESCE(dislikeCount, 0) + 1 WHERE id = :articleId")
    void incrementDislikeCount(@Param("articleId") String articleId);


    @Transactional
    @Modifying
    @Query("UPDATE Article SET dislikeCount = GREATEST(COALESCE(dislikeCount, 0) - 1, 0) WHERE id = :articleId")
    void decrementDislikeCount(@Param("articleId") String articleId);

}
