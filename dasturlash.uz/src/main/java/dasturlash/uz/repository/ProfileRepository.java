package dasturlash.uz.repository;

import dasturlash.uz.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    
    boolean existsByPhone(String phone);

    boolean existsByEmail(String email);
}
