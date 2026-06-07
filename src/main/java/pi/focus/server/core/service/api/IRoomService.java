package pi.focus.server.core.service.api;

import pi.focus.server.core.domain.Room;

import java.util.List;

public interface IRoomService {
    List<Room> getAllRooms();
}