package dasturlash.uz.config;

import dasturlash.uz.entity.Profile;
import dasturlash.uz.repository.ProfileRepository;
import dasturlash.uz.util.LoginIdentifierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private LoginIdentifierService identifierService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Profile profile = identifierService.identifyInputType(username);

        return new CustomUserDetails(profile) ;
    }

}
