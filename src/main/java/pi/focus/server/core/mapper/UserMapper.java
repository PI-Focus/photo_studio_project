package pi.focus.server.core.mapper;

import pi.focus.server.core.domain.User;
import pi.focus.server.core.entity.UserEntity;

import java.util.ArrayList;


public final class UserMapper {
    private UserMapper() {
    }

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
