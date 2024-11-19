package dasturlash.uz.util;

import dasturlash.uz.entity.Profile;
import dasturlash.uz.enums.LanguageEnum;
import dasturlash.uz.exceptions.DataNotFoundException;
import dasturlash.uz.repository.ProfileRepository;
import dasturlash.uz.service.ResourceBundleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LoginIdentifierService {

    private final ProfileRepository profileRepository;
    private final ResourceBundleService resourceBundleService;



    public Profile identifyInputType(String login, LanguageEnum lang) {

        boolean isEmail = login.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$");
        boolean isPhoneNumber = login.matches("^\\+?[0-9]{10,15}$");

        if (isEmail) {
            return getByEmailOrPhone(login, null, lang);
        } else if (isPhoneNumber) {
            return getByEmailOrPhone(null, login,lang);
        } else {
            throw new IllegalArgumentException(resourceBundleService.getMessage("invalid.login.format", lang));
        }


    }

    private Profile getByEmailOrPhone(String email, String phone, LanguageEnum lang) {

        if (email != null && !email.trim().isEmpty()) {
            Optional<Profile> profileWithEmail = profileRepository.findByEmailAndVisibleTrue(email);

            if (profileWithEmail.isEmpty()) {
                throw new DataNotFoundException(resourceBundleService.getMessage("email.not.found", lang));
            }
            return profileWithEmail.get();
        }

        if (phone != null && !phone.trim().isEmpty()) {
            Optional<Profile> profileWithPhone = profileRepository.findByPhoneAndVisibleTrue(phone);
            if (profileWithPhone.isEmpty()) {
                throw new DataNotFoundException(resourceBundleService.getMessage("phone.not.found", lang));
            }
            return profileWithPhone.get();
        }

        throw new RuntimeException(resourceBundleService.getMessage("something.went.wrong", lang));
    }
}
