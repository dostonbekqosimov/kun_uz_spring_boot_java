package dasturlash.uz.repository;

import dasturlash.uz.dtos.article.ArticleFullInfoDTO;
import dasturlash.uz.dtos.article.ArticleShortInfoDTO;
import dasturlash.uz.entity.article.Article;
import dasturlash.uz.enums.ArticleStatus;
import dasturlash.uz.repository.customInterfaces.ArticleFullInfoMapper;
import dasturlash.uz.repository.customInterfaces.ArticleShortInfoMapper;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    List<ArticleShortInfoMapper> findAllArticlesByIdList(@Param("status") ArticleStatus published, @Param("articleIdList") List<String> articleIdList);

    @Query("select a.id as id,  a.title as title, a.description as description, a.imageId AS imageId, a.publishedDate AS publishedDate " +
            "FROM Article a WHERE a.id IN :articleIdList AND a.id != :excludeArticleId AND a.status = :status AND a.visible = true " +
            "ORDER BY a.createdDate DESC")
    List<ArticleShortInfoMapper> findAllArticlesByIdListExcluding(@Param("status") ArticleStatus status,
                                                                  @Param("excludeArticleId") String excludeArticleId,
                                                                  @Param("articleIdList") List<String> articleIdList);

    @Query("SELECT a FROM Article a ORDER BY a.viewCount DESC")
    List<ArticleShortInfoMapper> findTopNMostReadArticles(Integer offset, Pageable pageable);


    @Query("SELECT a FROM Article a " +
            "WHERE a.status = :status " +
            "AND a.regionId = :regionId " +
            "AND a.id IN (SELECT atm.articleId FROM ArticleTypeMapping atm WHERE atm.articleTypeId = :articleTypeId) " +
            "ORDER BY a.createdDate DESC")
    List<ArticleShortInfoMapper> findTopNByArticleTypeAndRegionAndStatus(
            @Param("status") ArticleStatus status,
            @Param("articleTypeId") Long articleTypeId,
            @Param("regionId") Long regionId,
            Pageable pageable);


    @Query("Select a from Article a where a.regionId =?1")
    Page<ArticleShortInfoMapper> findAllByRegionId(Long regionId, Pageable pageable);

    @Query(" select a.id as id,  a.title as title, a.description as description, a.imageId as imageId, a.publishedDate as publishedDate " +
            "  From Article a where a.categoryId =?1 and a.status =?2 and a.visible = true order by a.createdDate desc")
    List<ArticleShortInfoMapper> findLastNArticlesByCategoryId(Long categoryId, ArticleStatus status, Pageable pageable);

    @Query("Select a from Article a where a.categoryId =?1")
    Page<ArticleShortInfoMapper> findAllByCategoryId(Long categoryId, Pageable pageable);


//    @Transactional
//    @Modifying
//    @Query("UPDATE Article SET viewCount = COALESCE(viewCount, 0) + 1 WHERE id = :id")
//    void updateViewCount(@Param("id") String id);
//
//    @Transactional
//    @Modifying
//    @Query("UPDATE Article SET sharedCount = COALESCE(sharedCount, 0) + 1 WHERE id = :id")
//    void updateShareCount(@Param("id") String id);

    @Query("""
                SELECT a.id AS id, a.title AS title, a.description AS description, a.content AS content, 
                       a.sharedCount AS sharedCount, a.viewCount AS viewCount, a.likeCount AS likeCount, 
                       a.publishedDate AS publishedDate,
                       r.id AS region_key, r.nameUz AS region_name,
                       c.id AS category_key, c.nameUz AS category_name,
                       t.name AS tagList_name
                FROM Article a
                LEFT JOIN a.region r
                LEFT JOIN a.category c
                LEFT JOIN ArticleTag at ON a.id = at.articleId
                LEFT JOIN at.tag t
                WHERE a.id = :articleId
            """)
    ArticleFullInfoMapper findArticleById(@Param("articleId") String articleId);


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
