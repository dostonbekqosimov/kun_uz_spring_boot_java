package dasturlash.uz.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestTemplateSmsService {

    private final RestTemplate restTemplate = new RestTemplate();

    public String sendMockSms(String to, String message) {
        // Mocky.io URL simulating SMS sending
        String mockUrl = "https://run.mocky.io/v3/bdc87702-d3e3-49d8-8994-c1cc1658836f";

        // You can also dynamically pass parameters if needed (e.g., 'to' and 'message')

        // Send GET request to Mocky.io and get the response
        String response = restTemplate.getForObject(mockUrl, String.class);

        return response;
    }
}

