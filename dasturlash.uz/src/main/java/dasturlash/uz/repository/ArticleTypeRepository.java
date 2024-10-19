package dasturlash.uz.repository;

import dasturlash.uz.entity.ArticleType;
import dasturlash.uz.enums.LanguageEnum;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ArticleTypeRepository extends JpaRepository<ArticleType, Long> {

    boolean existsByNameUzOrNameRuOrNameEn(String nameUz, String nameRu, String nameEn);


    Page<ArticleType> findAllByVisibleTrue(Pageable pageRequest);

    @Modifying
    @Transactional
    @Query("UPDATE ArticleType at SET at.visible = false WHERE at.id = :id")
    Integer changeVisible(Long id);

    @Query("SELECT at.id as id, at.orderNumber as orderNumber, " +
           "CASE WHEN :lang = dasturlash.uz.enums.LanguageEnum.UZ THEN at.nameUz " +
           "     WHEN :lang = dasturlash.uz.enums.LanguageEnum.RU THEN at.nameRu " +
           "     WHEN :lang = dasturlash.uz.enums.LanguageEnum.EN THEN at.nameEn END AS name, " +
           "FROM ArticleType at " +
           "WHERE at.visible = true " +
           "ORDER BY at.orderNumber")
    List<ArticleTypeProjection> findAllVisibleByLanguageOrdered(@Param("lang") LanguageEnum lang);
}
