package dasturlash.uz.repository;

import dasturlash.uz.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByNameUzOrNameRuOrNameEn(String nameUz, String nameRu, String nameEn);
}
