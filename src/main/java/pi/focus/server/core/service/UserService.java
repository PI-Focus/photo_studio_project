package pi.focus.server.core.service;

import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pi.focus.server.core.domain.User;
import pi.focus.server.core.entity.UserEntity;
import pi.focus.server.core.repository.UserRepository;
import pi.focus.server.core.service.api.IUserService;

import java.util.ArrayList;

/**
 * Сервис для управления пользователями и процессом регистрации.
 * Обрабатывает бизнес-логику создания учетных записей и проверки уникальности данных.
 */
@Service
@Profile({"dev", "prod", "test"})
public class UserService implements IUserService {
    private final UserRepository userRepository;
    /** Компонент для безопасного шифрования паролей */
    private final PasswordEncoder passwordEncoder;

    /**
     * Конструктор для внедрения зависимости репозитория.
     * @param userRepository репозиторий пользователя
     * @param passwordEncoder кодирование пароля
     * 
     */
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Создает нового пользователя в системе.
     * Перед сохранением проверяет логин на уникальность и шифрует пароль.
     * 
     * @param user данные нового пользователя из доменной модели
     * @return true, если пользователь успешно создан; false, если логин уже занят
     */
    @Override
    public boolean createUser(User user) {
        if (existsByLogin(user.login())) {
            return false;
        }
        userRepository.save(
                new UserEntity(
                        user.id(),
                        user.login(),
                        user.phoneNumber(),
                        user.email(),
                        passwordEncoder.encode(user.password()),
                        user.role(),
                        new ArrayList<>()
                )
        );
        return true;
    }

    private boolean existsByLogin(String login) {
        return userRepository.existsByLogin(login);
    }
}
