package pi.focus.server.core.domain;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

@SuppressWarnings({"PMD.AvoidLiteralsInIfCondition"})
public enum UserRole {
    USER,
    ADMIN;

    public SimpleGrantedAuthority toAuthority() {
        return new SimpleGrantedAuthority("ROLE_" + name());
    }

    public static UserRole toUserRole(String authority) {
        if ("ROLE_USER".equals(authority)) {
            return USER;
        } else if ("ROLE_ADMIN".equals(authority)) {
            return ADMIN;
        }
        return null;
    }
}
