package dasturlash.uz.repository;

import dasturlash.uz.dtos.article.ArticleShortInfoDTO;
import dasturlash.uz.entity.SavedArticles;
import dasturlash.uz.repository.customInterfaces.ArticleShortInfoMapper;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SavedArticleRepository extends JpaRepository<SavedArticles, String> {


    boolean existsByArticleIdAndProfileId(String articleId, Long currentUserId);

    Optional<SavedArticles> findByArticleIdAndProfileId(String articleId, Long profileId);

    @Transactional
    @Modifying
    @Query("UPDATE SavedArticles a SET a.visible = :visible WHERE a.articleId = :articleId AND a.profileId = :currentUserId")
    void changeVisibility(String articleId, Long currentUserId, Boolean visible);

    @Query("""
            SELECT a.id AS id,
                a.title AS title,
                a.description AS description,
                a.imageId AS imageId,
                a.publishedDate AS publishedDate
            FROM SavedArticles sa
            JOIN sa.article a
            WHERE sa.profileId = ?1
            AND sa.visible = TRUE
            ORDER BY a.createdDate DESC""")
    List<ArticleShortInfoMapper> findAllByProfileId(Long profileId);

}
