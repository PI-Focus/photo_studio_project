package pi.focus.server.core.service.mocks;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pi.focus.server.api.context.IPhotographersContext;
import pi.focus.server.core.service.api.IPhotographerService;
import pi.focus.server.service.context.mocks.PhotographersContextMock;

@Service
@Profile({"mock", "test"})
public class PhotographerServiceMock implements IPhotographerService {
    @Override
    public IPhotographersContext getEquipmentContext() {
        return new PhotographersContextMock();
    }
}
