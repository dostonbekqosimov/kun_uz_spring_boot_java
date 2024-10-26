package dasturlash.uz.repository;

import dasturlash.uz.entity.SmsHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SmsHistoryRepository extends JpaRepository<SmsHistory, Long> {
    List<SmsHistory> findByPhone(String phone);

    List<SmsHistory> findByCreatedDate(LocalDateTime date);
}
