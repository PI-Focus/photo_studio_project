package pi.focus.server.core.service;

import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pi.focus.server.core.domain.User;
import pi.focus.server.core.entity.UserEntity;
import pi.focus.server.core.repository.UserRepository;
import pi.focus.server.core.service.api.IUserService;

@Service
@Profile({"dev", "prod", "test"})
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

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
                        user.role()
                )
        );
        return true;
    }

    private boolean existsByLogin(String login) {
        return userRepository.existsByLogin(login);
    }
}
