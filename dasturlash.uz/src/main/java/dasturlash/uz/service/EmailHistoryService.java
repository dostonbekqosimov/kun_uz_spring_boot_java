package dasturlash.uz.service;

import dasturlash.uz.entity.EmailHistory;
import dasturlash.uz.repository.EmailHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmailHistoryService {

    @Autowired
    private EmailHistoryRepository emailHistoryRepository;

    public List<EmailHistory> getEmailHistoryByEmail(String email) {
        return emailHistoryRepository.findByEmail(email);
    }

    public List<EmailHistory> getEmailHistoryByDate(LocalDateTime date) {
        return emailHistoryRepository.findByCreatedDate(date);
    }

    public Page<EmailHistory> getEmailHistoryWithPagination(Pageable pageable) {
        return emailHistoryRepository.findAll(pageable);
    }
}

