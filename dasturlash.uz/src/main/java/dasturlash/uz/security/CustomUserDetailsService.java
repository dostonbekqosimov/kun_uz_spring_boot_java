package dasturlash.uz.security;

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


    @Override
    public UserDetails loadUserByUsername(String username) {





        Optional<Profile> optional = profileRepository.findByLoginAndVisibleTrue(username);

        if (optional.isEmpty()){
            throw new UnauthorizedException("Login or password is wrong");
        }

        Profile profile = optional.get();


        return new CustomUserDetails(profile);
    }

}
