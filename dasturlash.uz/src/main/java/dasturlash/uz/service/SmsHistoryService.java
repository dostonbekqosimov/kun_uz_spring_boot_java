package dasturlash.uz.service;

import dasturlash.uz.entity.SmsHistory;
import dasturlash.uz.repository.SmsHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SmsHistoryService {

    @Autowired
    private SmsHistoryRepository smsHistoryRepository;

    public List<SmsHistory> getSmsHistoryByPhone(String phone) {
        return smsHistoryRepository.findByPhone(phone);
    }

    public List<SmsHistory> getSmsHistoryByDate(LocalDateTime date) {
        return smsHistoryRepository.findByCreatedDate(date);
    }

    public Page<SmsHistory> getSmsHistoryWithPagination(Pageable pageable) {
        return smsHistoryRepository.findAll(pageable);
    }
}

