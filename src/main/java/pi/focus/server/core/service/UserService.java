package pi.focus.server.core.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pi.focus.server.core.domain.User;
import pi.focus.server.core.mapper.UserMapper;
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
    public boolean createUser(User user) {
        if (existsByLogin(user.login())) {
            return false;
        }
        userRepository.save(UserMapper.toEntity(user));
        return true;
    }

    private boolean existsByLogin(String login) {
        return userRepository.existsByLogin(login);
    }
}
