package pi.focus.server.core.mapper;

import pi.focus.server.core.domain.User;
import pi.focus.server.core.entity.UserEntity;

import java.util.ArrayList;

/**
 * Маппер для преобразования данных пользователей.
 * Служит мостом между и доменной моделью.
 */
public final class UserMapper {
    private UserMapper() {
    }

    /**
     * Переводит данные пользователя из БД в слой домена.
     * 
     * @param userEntity сущность пользователя
     * @return доменный объект User
     */
    public static User toDomain(UserEntity userEntity) {
        return new User(
                userEntity.getId(),
                userEntity.getLogin(),
                userEntity.getPassword(),
                userEntity.getPhoneNumber(),
                userEntity.getEmail(),
                userEntity.getRole()
        );
    }

    /**
     * Переводит данные пользователя из доменного слоя в сущность БД.
     * 
     * @param user доменный объект пользователя
     * @return сущность UserEntity с пустым списком бронирований
     */
    public static UserEntity toEntity(User user) {
        return new UserEntity(
                user.id(),
                user.login(),
                user.phoneNumber(),
                user.email(),
                user.password(),
                user.role(),
                new ArrayList<>()
        );
    }
}
