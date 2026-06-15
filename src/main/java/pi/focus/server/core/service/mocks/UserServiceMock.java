package pi.focus.server.core.service.mocks;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pi.focus.server.core.domain.User;
import pi.focus.server.core.service.api.IUserService;

/**
 * Мок-реализация сервиса пользователей.
 * В текущей версии всегда отклоняет регистрацию (возвращает false).
 */
@Service
@Profile({"mock", "test"})
public class UserServiceMock implements IUserService {
    /**
     * Имитирует попытку регистрации.
     * @return всегда false для проверки обработки ошибок регистрации
     */
    @Override
    public boolean createUser(User user) {
        return false;
    }
}
