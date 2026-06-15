package pi.focus.server.core.domain;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

/** 
 * Роли пользователей в системе, определяющие уровень доступа.
 */
@SuppressWarnings({"PMD.AvoidLiteralsInIfCondition"})
public enum UserRole {
    /** Обычный клиент: может просматривать каталоги и создавать бронирования */
    USER,
    /** Администратор: имеет доступ к управлению контентом и просмотру всех броней */
    ADMIN;
    
    /** 
     * Преобразует роль в формат Spring Security (SS).
     * @return объект полномочий для системы безопастности
     */
    public SimpleGrantedAuthority toAuthority() {
        return new SimpleGrantedAuthority("ROLE_" + name());
    }

    /** 
     * Конвертирует строковое представление роли из SS обратно в ENUM.
     * @param authority строка вида ROLE_USER или ROLE_ADMIN
     * @return соответствующий UserRole или null, если совпадений не найдено
     */
    public static UserRole toUserRole(String authority) {
        if ("ROLE_USER".equals(authority)) {
            return USER;
        } else if ("ROLE_ADMIN".equals(authority)) {
            return ADMIN;
        }
        return null;
    }
}
