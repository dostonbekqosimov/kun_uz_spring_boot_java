package dasturlash.uz.util;

import dasturlash.uz.config.security.CustomUserDetails;
import dasturlash.uz.enums.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SpringSecurityUtil {

    public static void checkRoleExists(String profileRole, Role... requiredRoles) {

    }

    public static CustomUserDetails getCurrentEntity() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetail = (CustomUserDetails) authentication.getPrincipal();

        return userDetail;
    }

    public static Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetail = (CustomUserDetails) authentication.getPrincipal();

        return userDetail.getId()   ;
    }
}
