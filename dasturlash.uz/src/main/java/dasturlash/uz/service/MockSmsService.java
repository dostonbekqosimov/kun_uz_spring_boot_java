package dasturlash.uz.service;

import dasturlash.uz.dtos.mockSmsDtos.SmsMessageDto;
import dasturlash.uz.dtos.mockSmsDtos.SmsResponseDto;
import dasturlash.uz.entity.SmsHistory;
import dasturlash.uz.enums.SmsStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class MockSmsService {
    private final RestTemplate restTemplate;
    private final String smsServiceUrl;
    private final MessageHistoryService messageHistoryService;

    @Autowired
    public MockSmsService(RestTemplate restTemplate,
                          @Value("${sms.service.url:http://localhost:8081}") String smsServiceUrl, MessageHistoryService messageHistoryService) {
        this.restTemplate = restTemplate;
        this.smsServiceUrl = smsServiceUrl;
        this.messageHistoryService = messageHistoryService;
    }

    public SmsResponseDto sendSms(String phoneNumber, String message, String verificationCode) {

        String endpoint = smsServiceUrl + "/api/sms/send";

        SmsMessageDto request = new SmsMessageDto();
        request.setTo(phoneNumber);
        request.setMessage(message);
        request.setFrom("Dark Web");

        try {
            ResponseEntity<SmsResponseDto> response = restTemplate.postForEntity(
                    endpoint,
                    request,
                    SmsResponseDto.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                SmsHistory existingHistory = messageHistoryService.findLatestSmsHistory(phoneNumber);
                messageHistoryService.updateSmsStatus(existingHistory, SmsStatus.SENT);
                log.info("SMS sent successfully to {}", phoneNumber);
                return response.getBody();
            } else {
                log.error("Failed to send SMS to {}", phoneNumber);
                throw new RuntimeException("Failed to send SMS");
            }
        } catch (RestClientException e) {
            log.error("Error sending SMS to {}: {}", phoneNumber, e.getMessage());
            SmsHistory existingHistory = messageHistoryService.findLatestSmsHistory(phoneNumber);
            messageHistoryService.updateSmsStatus(existingHistory, SmsStatus.FAILED);
            throw new RuntimeException("SMS service error", e);
        }
    }
}