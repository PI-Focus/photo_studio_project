package pi.focus.server.core.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pi.focus.server.core.domain.Room;
import pi.focus.server.core.entity.RoomEntity;
import pi.focus.server.core.repository.RoomRepository;
import pi.focus.server.core.service.api.IRoomService;

import java.util.List;

@Service
@Profile({"dev", "prod"})
public class RoomService implements IRoomService {
    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll().stream().map(this::toDomain).toList();
    }

    private Room toDomain(RoomEntity room) {
        return new Room(
                room.getId(),
                room.getTitle(),
                room.getDescription()
        );
    }
}
