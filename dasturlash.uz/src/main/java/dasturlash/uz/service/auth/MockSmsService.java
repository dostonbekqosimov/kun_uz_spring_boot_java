package dasturlash.uz.service.auth;

import dasturlash.uz.dtos.mockSMS.SmsMessageDto;
import dasturlash.uz.dtos.mockSMS.SmsResponseDto;
import dasturlash.uz.entity.SmsHistory;
import dasturlash.uz.enums.SmsStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Service
@Slf4j
public class MockSmsService {
    private final RestTemplate restTemplate;
    private final String smsServiceUrl;
    private final MessageHistoryService messageHistoryService;

    @Autowired
    public MockSmsService(RestTemplate restTemplate,
                          @Value("${sms.service.url:http://localhost:8081}") String smsServiceUrl,
                          MessageHistoryService messageHistoryService) {
        this.restTemplate = restTemplate;
        this.smsServiceUrl = smsServiceUrl;
        this.messageHistoryService = messageHistoryService;
    }

    public SmsResponseDto sendSms(String phoneNumber, String message, String verificationCode) {

        String endpoint = smsServiceUrl + "/api/sms/send";
        String login = "admin";  // Username for Basic Auth
        String password = "12213"; // Password for Basic Auth

        // Build the Authorization header with Basic Auth credentials
        HttpHeaders headers = new HttpHeaders();
        String credentials = login + ":" + password;
        String encodedAuth = Base64.getEncoder().encodeToString(credentials.getBytes());
        headers.set("Authorization", "Basic " + encodedAuth);

        SmsMessageDto request = new SmsMessageDto();
        request.setTo(phoneNumber);
        request.setMessage(message);
        request.setFrom("Dark Web");

        HttpEntity<SmsMessageDto> requestEntity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<SmsResponseDto> response = restTemplate.exchange(
                    endpoint,
                    HttpMethod.POST,
                    requestEntity,
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
