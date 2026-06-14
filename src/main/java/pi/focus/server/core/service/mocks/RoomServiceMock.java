package pi.focus.server.core.service.mocks;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pi.focus.server.api.context.IPhotoroomsContext;
import pi.focus.server.core.service.api.IRoomService;
import pi.focus.server.service.context.mocks.PhotoroomsContextMock;

@Service
@Profile({"mock", "test"})
public class RoomServiceMock implements IRoomService {
    @Override
    public IPhotoroomsContext getPhotoroomsContext() {
        return new PhotoroomsContextMock();
    }
}
