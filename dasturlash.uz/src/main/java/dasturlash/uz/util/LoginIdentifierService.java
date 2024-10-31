package dasturlash.uz.util;

import dasturlash.uz.entity.Profile;
import dasturlash.uz.exceptions.DataExistsException;
import dasturlash.uz.exceptions.DataNotFoundException;
import dasturlash.uz.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LoginIdentifierService {

    private final ProfileRepository profileRepository;


    public Profile identifyInputType(String login) {

        boolean isEmail = login.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$");
        boolean isPhoneNumber = login.matches("^\\+?[0-9]{10,15}$");

        if (isEmail) {
            return getByEmailOrPhone(login, null);
        } else if (isPhoneNumber) {
            return getByEmailOrPhone(null, login);
        } else {
            throw new IllegalArgumentException("Invalid login format. Please provide a valid email or phone number.");
        }


    }

    private Profile getByEmailOrPhone(String email, String phone) {

        if (email != null && !email.trim().isEmpty()) {
            Optional<Profile> profileWithEmail = profileRepository.findByEmailAndVisibleTrue(email);

            if (profileWithEmail.isEmpty()) {
                throw new DataNotFoundException("Profile with email: " + email + " not found");
            }
            return profileWithEmail.get();
        }

        if (phone != null && !phone.trim().isEmpty()) {
            Optional<Profile> profileWithPhone = profileRepository.findByPhoneAndVisibleTrue(phone);
            if (profileWithPhone.isEmpty()) {
                throw new DataNotFoundException("Profile with phone: " + phone + " not found");
            }
            return profileWithPhone.get();
        }

        throw new RuntimeException("Something wend wrong");
    }
}
