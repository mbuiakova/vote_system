package app.util;

import org.springframework.security.core.GrantedAuthority;

public class Role implements GrantedAuthority {

    boolean isAdmin = false;

    public Role(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    @Override
    public String getAuthority() {
        return this.isAdmin ? "ROLE_ADMIN" : "ROLE_USER";
    }
}
