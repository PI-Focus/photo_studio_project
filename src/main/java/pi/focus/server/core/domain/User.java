package pi.focus.server.core.domain;

import java.util.UUID;

/**
 * Модель пользователя системы.
 * @param id уникальный идентификатор
 * @param login имя пользователя для входа
 * @param phoneNumber контактный телефон
 * @param email электронная почта
 * @param password хэш пароля
 * @param role роль в системе (пользователь/админ)
 */
public record User(
        UUID id,
        String login,
        String phoneNumber,
        String email,
        String password,
        UserRole role
) { }