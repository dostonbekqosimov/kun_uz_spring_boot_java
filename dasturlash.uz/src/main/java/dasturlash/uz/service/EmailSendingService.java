package dasturlash.uz.service;

import dasturlash.uz.dtos.profileDTOs.MessageDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSendingService {

    @Value("${spring.mail.username}")
    private String fromAccount;
    @Autowired
    private final JavaMailSender javaMailSender;

    public String sendMimeMessage(MessageDTO dto) {
        try {
            MimeMessage msg = javaMailSender.createMimeMessage();
            msg.setFrom(fromAccount);
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);

            helper.setTo(dto.getToAccount());
            helper.setSubject(dto.getSubject());
            helper.setText(dto.getText(), true);
            javaMailSender.send(msg);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        return "Mail was sent";
    }

}
