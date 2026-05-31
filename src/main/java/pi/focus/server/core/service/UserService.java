package pi.focus.server.core.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pi.focus.server.core.domain.User;
import pi.focus.server.core.entity.UserEntity;
import pi.focus.server.core.repository.UserRepository;
import pi.focus.server.core.service.api.IUserService;

@Service
@Profile({"dev", "prod"})
public class UserService implements IUserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void createUser(User user) {
        userRepository.save(toEntity(user));
    }

    private User toDomain(UserEntity user) {
        return new User(
                user.getId(),
                user.getLogin(),
                user.getPassword(),
                user.getRole()
        );
    }

    private UserEntity toEntity(User user) {
        return new UserEntity(
                user.id(),
                user.login(),
                user.password(),
                user.role()
        );
    }
}
