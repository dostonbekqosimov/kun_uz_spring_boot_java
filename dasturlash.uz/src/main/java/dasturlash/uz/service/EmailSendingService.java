package dasturlash.uz.service;

import dasturlash.uz.dtos.profileDTOs.MessageDTO;
import dasturlash.uz.entity.EmailHistory;
import dasturlash.uz.entity.Profile;
import dasturlash.uz.repository.EmailHistoryRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmailSendingService {

    @Value("${spring.mail.username}")
    private String fromAccount;

    private final JavaMailSender javaMailSender;
    private final EmailHistoryRepository emailHistoryRepository;

    public String sendMimeMessage(MessageDTO dto, Profile profile) {

        EmailHistory history = new EmailHistory();
        history.setToAccount(dto.getToAccount());
        history.setSubject(dto.getSubject());
        history.setMessage(dto.getText());
        history.setSentAt(LocalDateTime.now());
        history.setProfile(profile);
        history.setStatus("PENDING"); // Set initial status


        try {
            MimeMessage msg = javaMailSender.createMimeMessage();
            msg.setFrom(fromAccount);
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);

            helper.setTo(dto.getToAccount());
            helper.setSubject(dto.getSubject());
            helper.setText(dto.getText(), true);
            javaMailSender.send(msg);

            // Update status to SUCCESS after successful send
            history.setStatus("SUCCESS");
            emailHistoryRepository.save(history);

            return "Mail was sent successfully";
        } catch (MessagingException e) {
            history.setStatus("FAILED");
            history.setErrorMessage(e.getMessage());
            emailHistoryRepository.save(history);

            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
        }
    }

}
