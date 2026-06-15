package pi.focus.server.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pi.focus.server.core.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для управления пользователями системы.
 */
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    /**
     * Поиск пользователя по логину
     * @param login логин пользователя
     * @return Optional содержащий пользователя, если он найден
     */
    Optional<UserEntity> findByLogin(String login);

    /**
     * Поиск пользователя по номеру телефона.
     * @param phoneNumber номер телефона
     * @return Optional, содержащий пользователя, если он найден
     */
    Optional<UserEntity> findByPhoneNumber(String phoneNumber);
    /**
     * Поиск пользователя по адресу электронной почты
     * @param email адрес почты
     * @return Optional, содержащий пользователя, если он найден
     */
    Optional<UserEntity> findByEmail(String email);
    
    /**
     * Проверка существования пользователя с данным логином.
     * @param login логин для проверки
     * @return true, если логин уже занят; false, если свободен
     */
    Boolean existsByLogin(String login);
    Boolean existsByPhoneNumber(String phoneNumber);
    Boolean existsByEmail(String email);
}
