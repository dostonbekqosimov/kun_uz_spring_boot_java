package dasturlash.uz.repository;


import dasturlash.uz.entity.EmailHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EmailHistoryRepository extends JpaRepository<EmailHistory, Long> {
    List<EmailHistory> findByProfileIdOrderBySentAtDesc(Long profileId);
    List<EmailHistory> findByToAccountOrderBySentAtDesc(String email);

    List<EmailHistory> findByEmail(String email);

    List<EmailHistory> findByCreatedDate(LocalDateTime date);
}
