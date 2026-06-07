package pi.focus.server.core.service.mocks;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pi.focus.server.core.domain.Room;
import pi.focus.server.core.service.api.IRoomService;

import java.util.List;

@Service
@Profile({"mock", "test"})
public class RoomServiceMock implements IRoomService {
    @Override
    public List<Room> getAllRooms() {
        return List.of();
    }
}
