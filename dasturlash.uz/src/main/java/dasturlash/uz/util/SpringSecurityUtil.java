package dasturlash.uz.util;

import dasturlash.uz.security.CustomUserDetails;
import dasturlash.uz.dtos.ProfileShortInfoDTO;
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

    public static Long getCurrentUserId() {
        CustomUserDetails userDetail = getCurrentEntity();

        return userDetail.getId();
    }

    public static ProfileShortInfoDTO getCurrentUserShortInfo() {
        CustomUserDetails userDetail = getCurrentEntity();

        ProfileShortInfoDTO shortDetail = new ProfileShortInfoDTO();
        shortDetail.setProfileId(userDetail.getId());
        shortDetail.setName(userDetail.getName());
        shortDetail.setSurname(userDetail.getSurname());

        return shortDetail;

    }

    public static Role getCurrentUserRole(){

        CustomUserDetails userDetail = getCurrentEntity();

        return userDetail.getRole();
    }
}
