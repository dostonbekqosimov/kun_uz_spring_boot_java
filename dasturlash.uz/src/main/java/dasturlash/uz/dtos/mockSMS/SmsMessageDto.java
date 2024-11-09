package dasturlash.uz.dtos.mockSMS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsMessageDto {
    private String to;
    private String from;
    private String message;
}
