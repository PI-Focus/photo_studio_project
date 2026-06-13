package pi.focus.server.core.domain;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum UserRole {
    USER,
    ADMIN;

    public SimpleGrantedAuthority toAuthority() {
        return new SimpleGrantedAuthority("ROLE_" + name());
    }

    public static UserRole toUserRole(String authority) {
        if (authority.equals("ROLE_USER")) {
            return UserRole.USER;
        } else if (authority.equals("ROLE_ADMIN")) {
            return UserRole.ADMIN;
        }
        return null;
    }
}
