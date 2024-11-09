package dasturlash.uz.repository;

import dasturlash.uz.entity.article.ArticleTypeMapping;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArticleTypeMapRepository extends JpaRepository<ArticleTypeMapping, String> {

    @Query("select articleTypeId from ArticleTypeMapping where articleId =?1")
    List<Long> findAllByArticleId(String articleId);

    @Modifying
    @Transactional
    @Query("delete from ArticleTypeMapping where articleId =?1")
    void deleteByArticleId(String articleId);

    @Modifying
    @Transactional
    @Query("delete from ArticleTypeMapping where articleId =?1 and articleTypeId = ?2")
    void deleteByArticleIdAndTypeId(String articleId, Long articleTypeId);
}
