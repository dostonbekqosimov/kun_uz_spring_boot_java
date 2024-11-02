package dasturlash.uz.controller;

import dasturlash.uz.entity.SmsHistory;
import dasturlash.uz.service.auth.SmsHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/smsHistory")
public class SmsHistoryController {

    @Autowired
    private SmsHistoryService smsHistoryService;

    // Get SmsHistory by phone
    @GetMapping("/by-phone")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<SmsHistory>> getSmsHistoryByPhone(@RequestParam String phone) {
        List<SmsHistory> smsHistoryList = smsHistoryService.getSmsHistoryByPhone(phone);
        if (smsHistoryList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(smsHistoryList);
    }

    // Get SmsHistory by given date
    @GetMapping("/by-date")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<SmsHistory>> getSmsHistoryByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        List<SmsHistory> smsHistoryList = smsHistoryService.getSmsHistoryByDate(date);
        if (smsHistoryList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(smsHistoryList);
    }

    // Get SmsHistory with pagination (Admin)
    @GetMapping("/paginated")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<SmsHistory>> getSmsHistoryPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page -1, size);
        Page<SmsHistory> smsHistoryPage = smsHistoryService.getSmsHistoryWithPagination(pageable);

        if (smsHistoryPage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(smsHistoryPage);
    }
}

