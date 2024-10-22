package dasturlash.uz.repository;

import dasturlash.uz.entity.ArticleType;
import dasturlash.uz.entity.Profile;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    
    boolean existsByPhone(String phone);

    boolean existsByEmail(String email);

    Optional<Profile> findByIdAndVisibleTrue(Long id);

    Page<Profile> findAllByVisibleTrue(Pageable pageRequest);

    @Modifying
    @Transactional
    @Query("UPDATE Profile at SET at.visible = false WHERE at.id = :id")
    Integer changeVisible(Long id);
}
