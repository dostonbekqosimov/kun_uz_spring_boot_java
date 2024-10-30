package dasturlash.uz.repository;

import dasturlash.uz.entity.SmsHistory;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SmsHistoryRepository extends JpaRepository<SmsHistory, String> {

    Optional<SmsHistory> findTopByPhoneOrderByCreatedDateDesc(String phone);

    List<SmsHistory> findByPhone(String phone);

    List<SmsHistory> findByCreatedDate(LocalDateTime date);

    @Query("select count(s) from SmsHistory s where s.phone = ?1 and s.createdDate between ?2 and ?3")
    Long getSmsCount(String phone, LocalDateTime from, LocalDateTime to);

    @Modifying
    @Transactional
    @Query("update SmsHistory set attemptCount = attemptCount + 1 where phone = ?1")
    void increaseAttemptCount(String phone);
}
