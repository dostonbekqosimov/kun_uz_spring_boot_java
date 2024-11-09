package dasturlash.uz.config.security;

import dasturlash.uz.entity.Profile;
import dasturlash.uz.exceptions.UnauthorizedException;
import dasturlash.uz.repository.ProfileRepository;
import dasturlash.uz.util.LoginIdentifierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private LoginIdentifierService identifierService;

    @Override
    public UserDetails loadUserByUsername(String username) {


//        Profile profile = identifierService.identifyInputType(username);


        Optional<Profile> optional = profileRepository.findByLoginAndVisibleTrue(username);

        if (optional.isEmpty()){
            throw new UnauthorizedException("Login or password is wrong");
        }

        Profile profile = optional.get();


//        Profile profile1 = profileRepository.findByPhoneOrEmail(username)
//                .orElseThrow(() -> new DataNotFoundException("Invalid login format. Please provide a valid email or phone number."));

        return new CustomUserDetails(profile);
    }

}
