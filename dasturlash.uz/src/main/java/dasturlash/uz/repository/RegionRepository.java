package dasturlash.uz.repository;

import dasturlash.uz.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<Region, Long> {
    boolean existsByNameUzOrNameRuOrNameEn(String nameUz, String nameRu, String nameEn);
}
