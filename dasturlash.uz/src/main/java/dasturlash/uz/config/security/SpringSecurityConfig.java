package dasturlash.uz.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SpringSecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {


        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {


        http.authorizeHttpRequests(hrr -> {
                    hrr
                            // Authentication APIs - open to all
                            .requestMatchers("/auth/**").permitAll()
                            .requestMatchers("/category/lang").permitAll()
                            .requestMatchers("/article-type/lang").permitAll()
                            .requestMatchers("/region/lang").permitAll()
                            .requestMatchers("/attach/**").permitAll()
                            .requestMatchers("/post", "/post/**").permitAll()
                            .requestMatchers("/article", "/article/**").permitAll()
                            .requestMatchers("/like/**").hasAnyRole("USER", "ADMIN", "MODERATOR", "PUBLISHER")
                            .requestMatchers("/comments/article/*/comments").permitAll()


                            .anyRequest()
                            .authenticated();
                })
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(httpSecurityCorsConfigurer -> {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOriginPatterns(Arrays.asList("*"));
            configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
            configuration.setAllowedHeaders(Arrays.asList("*"));
            configuration.setAllowCredentials(true); // Add this line

            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", configuration);
            httpSecurityCorsConfigurer.configurationSource(source);
        });


        return http.build();
    }

}


//                            // Profile APIs
//                            .requestMatchers(HttpMethod.POST, "/profile").hasRole("ADMIN")
//                            .requestMatchers(HttpMethod.PUT, "/profile/*/admin").hasRole("ADMIN")
//                            .requestMatchers(HttpMethod.GET, "/profile", "/profile/").hasRole("ADMIN")
//                            .requestMatchers(HttpMethod.DELETE, "/profile/*").hasRole("ADMIN")
//
////                             Article Type APIs
//                            .requestMatchers(HttpMethod.POST, "/article-type").hasRole("ADMIN")
//                            .requestMatchers(HttpMethod.PUT, "/article-type/{id}").hasRole("ADMIN")
//                            .requestMatchers(HttpMethod.DELETE, "/article-type/{id}").hasRole("ADMIN")
//                            .requestMatchers(HttpMethod.GET, "/article-type").hasRole("ADMIN")
//                            .requestMatchers(HttpMethod.GET, "/article-type/lang").permitAll()
//
//                            // Region APIs
//                            .requestMatchers(HttpMethod.POST, "/region").hasRole("ADMIN")
//                            .requestMatchers(HttpMethod.PUT, "/region/{id}").hasRole("ADMIN")
//                            .requestMatchers(HttpMethod.DELETE, "/region/{id}").hasRole("ADMIN")
//                            .requestMatchers(HttpMethod.GET, "/region").hasRole("ADMIN")
//                            .requestMatchers(HttpMethod.GET, "/region/lang").permitAll()
//
//                            // Category APIs
//                            .requestMatchers(HttpMethod.POST, "/category").hasRole("ADMIN")
//                            .requestMatchers(HttpMethod.PUT, "/category/{id}").hasRole("ADMIN")
//                            .requestMatchers(HttpMethod.DELETE, "/category/{id}").hasRole("ADMIN")
//                            .requestMatchers(HttpMethod.GET, "/category").hasRole("ADMIN")
//                            .requestMatchers(HttpMethod.GET, "/category/lang").permitAll()
//
//                            // Article APIs
//                            .requestMatchers(HttpMethod.POST, "/article").hasRole("MODERATOR")
//                            .requestMatchers(HttpMethod.PUT, "/article/{id}").hasRole("MODERATOR")
//                            .requestMatchers(HttpMethod.DELETE, "/article/{id}").hasRole("MODERATOR")
//                            .requestMatchers(HttpMethod.PUT, "/article/change-status/{id}").hasRole("PUBLISHER")
//                            .requestMatchers(HttpMethod.GET, "/article/filter").hasRole("PUBLISHER")
//
//                            // Article Like APIs
//                            .requestMatchers(HttpMethod.POST, "/article-like").authenticated()
//                            .requestMatchers(HttpMethod.DELETE, "/article-like").authenticated()
//
//
//                            // SMS and Email History routes
//                            .requestMatchers("/api/smsHistory/paginated").hasRole("ADMIN")
//                            .requestMatchers("/api/emailHistory/paginated").hasRole("ADMIN")
//
//                            .requestMatchers("/api/smsHistory/paginated").hasRole("ADMIN")
//                            .requestMatchers("/api/emailHistory/paginated").hasRole("ADMIN")
