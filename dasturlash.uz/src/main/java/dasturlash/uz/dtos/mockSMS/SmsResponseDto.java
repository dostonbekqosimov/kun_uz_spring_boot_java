package dasturlash.uz.dtos.mockSMS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsResponseDto {
    private String messageId;
    private String status;
    private LocalDateTime timestamp;
}
