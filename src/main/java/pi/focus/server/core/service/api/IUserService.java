package pi.focus.server.core.service.api;

import pi.focus.server.core.domain.User;

/**
 * Интерфейс сервиса для управления пользователями и их учетными данными.
 */
public interface IUserService {
    /**
     * Регистрирует нового пользователя в системе.
     * 
     * @param user объект доменной модели пользователя с данными для регистрации
     * @return true, если пользователь успешно создан; false, если возникла ошибка (например, логин занят)
     */
    boolean createUser(User user);  
}
