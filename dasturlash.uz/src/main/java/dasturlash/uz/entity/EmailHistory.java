package dasturlash.uz.entity;



import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "email_history")
public class EmailHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String toAccount;

    @Column(nullable = false)
    private String subject;

    @Column(columnDefinition = "TEXT", length = 65535)
    private String message;

    @Column(nullable = false)
    private LocalDateTime sentAt;

    private String status; // SUCCESS, FAILED

    @Column(columnDefinition = "TEXT")
    private String errorMessage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;
}
