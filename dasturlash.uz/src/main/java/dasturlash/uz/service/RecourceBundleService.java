package dasturlash.uz.service;

import dasturlash.uz.enums.LanguageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class RecourceBundleService {
    @Autowired
    private ResourceBundleMessageSource resourceBundleMessageSource;

    public String getMessage(String code,LanguageEnum lang) {
        return resourceBundleMessageSource.getMessage(code, null, new Locale(lang.name()));
    }

}
