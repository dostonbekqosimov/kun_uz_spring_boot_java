package dasturlash.uz.security;

import dasturlash.uz.dtos.token.JwtDTO;
import dasturlash.uz.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // Continue the filter chain
            return;
        }

        try {
            final String token = header.substring(7).trim();
            JwtDTO dto = JwtUtil.decode(token);


            // load user depending on role
            String login = dto.getLogin();

            UserDetails userDetails = userDetailsService.loadUserByUsername(login);


            System.out.println("JWT Role: " + dto.getRole());
            System.out.println("User Details Authorities: " + userDetails.getAuthorities());


            // Savol ustozga: Bu yerda biz roleni JWTdan mas balki userdetailsadan beryabmizmi, shuni o'zgartirish kerakmi?

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
        } catch (JwtException | UsernameNotFoundException e) {
            filterChain.doFilter(request, response); // Continue the filter chain
            return;
        }
    }

}
