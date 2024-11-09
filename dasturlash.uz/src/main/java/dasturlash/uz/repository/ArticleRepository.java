package dasturlash.uz.repository;

import dasturlash.uz.entity.article.Article;
import dasturlash.uz.enums.ArticleStatus;
import dasturlash.uz.repository.customInterfaces.ArticleShortInfoMapper;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, String> {

    @Query(" select a.id as id,  a.title as title, a.description as description, a.imageId as imageId, a.publishedDate as publishedDate " +
           "  From Article a  where  a.id not in ?2 and a.status =?1 and a.visible = true order by a.createdDate desc")
    List<ArticleShortInfoMapper> findLast8ArticlesExcluding(ArticleStatus status,
                                                List<String> excludeIdList, Pageable pageable);



    Optional<Article> findByIdAndVisibleTrue(String articleId);


    @Modifying
    @Transactional
    @Query("UPDATE Article at SET at.visible = false WHERE at.id = :articleId ")
    Integer changeVisibility(String articleId);



    @Query("SELECT a.id AS id, a.title AS title, a.description AS description, a.imageId AS imageId, a.publishedDate AS publishedDate " +
           "FROM Article a WHERE a.id IN :articleIdList AND a.status = :status AND a.visible = true " +
           "ORDER BY a.createdDate DESC")
    List<ArticleShortInfoMapper> findAllArticlesByIdList(@Param("status")ArticleStatus published, @Param("articleIdList")List<String> articleIdList);
}
