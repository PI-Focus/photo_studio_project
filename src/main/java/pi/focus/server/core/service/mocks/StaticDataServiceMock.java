package pi.focus.server.core.service.mocks;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pi.focus.server.api.context.IInfoContext;
import pi.focus.server.core.service.api.IStaticDataService;
import pi.focus.server.service.context.mocks.InfoContextMock;

@Service
@Profile({"mock", "test"})
public class StaticDataServiceMock implements IStaticDataService {
    @Override
    public IInfoContext getInfo() {
        return new InfoContextMock();
    }
}
