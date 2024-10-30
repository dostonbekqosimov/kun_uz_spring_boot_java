package dasturlash.uz.entity;

import dasturlash.uz.enums.SmsStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
public class SmsHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String phone;
    private String message;

    @Enumerated(value = EnumType.STRING)
    private SmsStatus status;
    private String verificationCode;
    private Integer attemptCount;
    private LocalDateTime createdDate;
}