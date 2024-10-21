package dasturlash.uz.repository;

import dasturlash.uz.entity.Region;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RegionRepository extends JpaRepository<Region, Long> {
    boolean existsByNameUzOrNameRuOrNameEn(String nameUz, String nameRu, String nameEn);

    Optional<Region> findByIdAndVisibleTrue(Long id);

    boolean existsByOrderNumber(@NotNull(message = "orderNumber cannot be null") Integer orderNumber);

    @Modifying
    @Transactional
    @Query("UPDATE Region at SET at.visible = false WHERE at.id = :id")
    Integer changeVisible(Long id);

    @Query(value = "select id as id , order_number as orderNumber, " +
                   "       case ?1 " +
                   "           when 'uz' then name_uz " +
                   "           when 'en' then name_en " +
                   "        else name_ru " +
                   "        end as name " +
                   "From regions " +
                   " where visible is true  " +
                   " order by order_number;", nativeQuery = true)
    List<CustomMapperInterface> findAllVisibleByLanguageOrdered(String name);
}
