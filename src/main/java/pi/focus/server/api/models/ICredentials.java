package pi.focus.server.api.models;

/**
 * Интерфейс для передачи учетных данных пользователя.
 * Используется при аутентификации и обновлении профиля.
 */
public interface ICredentials {
    /** @return логин пользователя */
    String getLogin();
    /** @return пароль пользователя */
    String getPassword();
    /** @return номер телефона пользователя */
    String getPhoneNumber();
    /** @return адрес электронной почты */
    String getEmail();
    /** @return сообщение об ошибке валидации данных (если есть) */
    String getError();
    /** @param error текст ошибки для передачи на сторону клиента */
    void setError(String error);
}
