package dasturlash.uz.config;

import dasturlash.uz.entity.Profile;
import dasturlash.uz.repository.ProfileRepository;
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Profile> optional = profileRepository.findByEmailAndVisibleTrue(username);
        if (optional.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        Profile profile = optional.get();
        return new CustomUserDetails(profile) ;
    }

}
