package dasturlash.uz.entity;


import dasturlash.uz.enums.EmailStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "email_history")
public class EmailHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String toAccount;

    @Column(nullable = false)
    private String subject;

    @Column(columnDefinition = "TEXT", length = 65535)
    private String message;

    @Column(nullable = false)
    private LocalDateTime sentAt;

    @Enumerated(value = EnumType.STRING)
    private EmailStatus status;

    private Integer attemptCount = 0;


    @Column(columnDefinition = "TEXT")
    private String errorMessage;

    private LocalDateTime createdDate;

    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;
}
