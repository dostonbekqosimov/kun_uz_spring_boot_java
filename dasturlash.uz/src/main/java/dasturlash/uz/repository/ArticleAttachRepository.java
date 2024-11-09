package dasturlash.uz.repository;

import dasturlash.uz.entity.article.ArticleAttach;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArticleAttachRepository extends JpaRepository<ArticleAttach, String> {

    @Query("select attachId from ArticleAttach where articleId =?1")
    List<String> findAllByArticleId(String  articleId);

    @Modifying
    @Transactional
    @Query("delete from ArticleAttach where articleId =?1")
    void deleteByArticleId(String articleId);

    @Modifying
    @Transactional
    @Query("delete from ArticleAttach where articleId =?1 and attachId = ?2")
    void deleteByArticleIddAndAttachId(String articleId, String attachId);

}
