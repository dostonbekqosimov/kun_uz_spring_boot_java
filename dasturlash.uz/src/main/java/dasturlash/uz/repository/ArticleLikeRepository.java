package dasturlash.uz.repository;

import dasturlash.uz.entity.ArticleLike;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ArticleLikeRepository extends JpaRepository<ArticleLike, String> {


    boolean existsByProfileIdAndArticleIdAndVisibleTrue(Long profileId, String articleId);


    @Transactional
    @Modifying
    @Query("UPDATE ArticleLike a SET a.visible = :visible WHERE a.profileId = :profileId AND a.articleId = :articleId")
    Integer changeVisibility(@Param("articleId") String articleId, @Param("visible") Boolean visible, @Param("profileId") Long profileId);

    boolean existsByProfileIdAndArticleId(Long profileId, String articleId);

    Optional<ArticleLike> findByProfileIdAndArticleId(Long profileId, String articleId);

    Optional<ArticleLike> findByProfileIdAndArticleIdAndVisibleTrue(Long profileId, String articleId);
}

