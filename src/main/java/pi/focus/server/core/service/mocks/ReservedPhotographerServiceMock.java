package pi.focus.server.core.service.mocks;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pi.focus.server.core.service.api.IReservedPhotographerService;

@Service
@Profile({"mock", "test"})
public class ReservedPhotographerServiceMock implements IReservedPhotographerService {
}
