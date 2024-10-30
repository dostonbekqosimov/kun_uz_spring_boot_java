package dasturlash.uz.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SmsConfig {
    @Value("${sms.service.url:http://localhost:8081}")  // Default to localhost:8081
    private String smsServiceUrl;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}