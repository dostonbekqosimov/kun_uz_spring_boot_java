package dasturlash.uz.repository;

import dasturlash.uz.entity.Profile;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {



    
    boolean existsByPhone(String phone);

    boolean existsByEmail(String email);

    Optional<Profile> findByIdAndVisibleTrue(Long id);

    Page<Profile> findAllByVisibleTrue(Pageable pageRequest);

//    @Query(value = "SELECT * FROM profiles p WHERE " +
//           "(p.phone = :login AND :login ~ '^[0-9]+$') OR " +
//           "(p.email = :login AND :login ~ '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$')", nativeQuery = true)
//    Optional<Profile> findByPhoneOrEmail(@Param("login") String login);

    @Modifying
    @Transactional
    @Query("UPDATE Profile at SET at.visible = false WHERE at.id = :id")
    Integer changeVisible(Long id);



    @Query("SELECT p FROM Profile p WHERE p.phone = :phone AND p.visible = true")
    Optional<Profile> findByPhoneAndVisibleTrue(@Param("phone") String phone);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Profile p WHERE p.phone = :phone AND p.visible = true")
    boolean existsByPhoneAndVisibleTrue(@Param("phone") String phone);

    // Keep your existing methods
    Optional<Profile> findByEmailAndVisibleTrue(String email);

    boolean existsByEmailAndVisibleTrue(String email);

    Optional<Profile> findByLoginAndVisibleTrue(String username);
}
