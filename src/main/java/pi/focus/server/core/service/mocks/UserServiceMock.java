package pi.focus.server.core.service.mocks;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pi.focus.server.core.domain.User;
import pi.focus.server.core.service.api.IUserService;

@Service
@Profile({"mock", "test"})
public class UserServiceMock implements IUserService {
    @Override
    public boolean createUser(User user) {
        return false;
    }
}
