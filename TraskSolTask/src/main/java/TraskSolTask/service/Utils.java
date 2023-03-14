package TraskSolTask.service;

import TraskSolTask.model.users.AuthRole;
import TraskSolTask.security.services.UserDetailsImpl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class Utils {


    public static UserDetailsImpl getCurrent() {
        return (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
    }


    public static AuthRole getCurrentAuthRole() {

        String authRole = getCurrent().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList().get(0);

        if (authRole.equals(AuthRole.ADMIN.toString())) {
            return AuthRole.ADMIN;
        }
        return AuthRole.USER;
    }


    public static String getCurrentEmail() {
        return getCurrent().getEmail();
    }
}
