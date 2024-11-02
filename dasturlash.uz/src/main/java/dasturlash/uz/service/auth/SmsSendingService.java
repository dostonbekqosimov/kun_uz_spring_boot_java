package dasturlash.uz.service.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import dasturlash.uz.dtos.SmsAuthResponseDTO;
import dasturlash.uz.repository.SmsHistoryRepository;
import dasturlash.uz.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SmsSendingService {

    private final SmsHistoryRepository smsHistoryRepository;

    public void sendRegistrationSms(String phoneNumber) {

        int code = RandomUtil.getRandomInt();
        String message = "<#>kitabu.uz partali. Ro'yxatdan o'tish uchun tasdiqlash kodi: " + code;
        sendSms(phoneNumber, message);
    }


    private void sendSms(String phone, String message) {
        try {
            // TODO limit 3
            Long smsCount = smsHistoryRepository.getSmsCount(phone, LocalDateTime.now().minusMinutes(1), LocalDateTime.now());
            // TODO save

            RequestBody formBody = new FormBody.Builder()
                    .add("mobile_phone", phone)
                    .add("message", message)
                    .add("from", "4546")
                    .build();

            Request request = new Request.Builder()
                    .url("https://notify.eskiz.uz/api/message/sms/send")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "Bearer " + getToken())
                    .post(formBody)
                    .build();

            OkHttpClient client = new OkHttpClient();
            Call call = client.newCall(request);

            Response response = call.execute();
            System.out.println(response.body().string());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private String getToken() {
        // repository
        // TODO
        return "eyVCJ9.aass.a2QB";
    }

    private String getNewToken() {
        try {
            RequestBody formBody = new FormBody.Builder()
                    .add("email", "emailfromsdasdasdasd ")
                    .add("password", "13123123123")
                    .add("from", "4546")
                    .build();

            Request request = new Request.Builder()
                    .url("https://notify.eskiz.uz/api/auth/login")
                    .addHeader("Content-Type", "application/json")
                    .post(formBody)
                    .build();

            OkHttpClient client = new OkHttpClient();
            Call call = client.newCall(request);

            Response response = call.execute();
            String json = response.body().string();

            ObjectMapper mapper = new ObjectMapper();
            SmsAuthResponseDTO result = mapper.readValue(json, SmsAuthResponseDTO.class);
            return result.getData().getToken();
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new RuntimeException();
    }

    public void check(String phoneNumber, String code) {
        // 3. check code is correct
        // 4. sms expiredTime
        // 5. attempt count
    }
}
