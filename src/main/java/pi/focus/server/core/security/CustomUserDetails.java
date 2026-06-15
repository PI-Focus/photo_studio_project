package pi.focus.server.core.security;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;

/**
 * Пользовательская реализация интерфейса UserDetails для системы безопасности Spring Security.
 * Представляет собой объект-контейнер , который хранит расширенную информацию о пользователе, 
 * включая его идентификатор, контактные данные и полномочия (роли).
 * 
 * @param username имя пользователя для системы аутентификации
 * @param password хешированный пароль пользователя
 * @param authorities коллекция предоставленных прав доступа (ролей)
 * @param userId уникальный системный идентификатор пользователя (UUID)
 * @param login логин пользователя для отображения в интерфейсе
 * @param email адрес электронной почты пользователя
 * @param phoneNumber контактный номер телефона
 */
public record CustomUserDetails(
        String username,
        String password,
        Collection<? extends GrantedAuthority> authorities,
        UUID userId,
        String login,
        String email,
        String phoneNumber
) implements UserDetails {
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public @Nullable String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
