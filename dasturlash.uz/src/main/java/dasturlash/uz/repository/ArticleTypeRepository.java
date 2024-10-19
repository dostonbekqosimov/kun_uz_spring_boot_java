package dasturlash.uz.repository;

import dasturlash.uz.entity.ArticleType;
import dasturlash.uz.enums.LanguageEnum;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
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

    @Query(value = "select id as id , order_number as orderNumber, " +
                   "       case ?1 " +
                   "           when 'uz' then name_uz " +
                   "           when 'en' then name_en " +
                   "        else name_ru " +
                   "        end as name " +
                   "From article_types " +
                   " where visible is true  " +
                   " order by order_number;", nativeQuery = true)
    List<ArticleTypeProjection> findAllVisibleByLanguageOrdered(String lang);

    boolean existsByOrderNumber(@NotNull(message = "orderNumber cannot be null") Integer orderNumber);
}
