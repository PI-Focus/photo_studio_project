package pi.focus.server.core.mapper;

import pi.focus.server.core.domain.Room;
import pi.focus.server.core.entity.RoomEntity;

import java.util.ArrayList;


public final class RoomMapper {
    private RoomMapper() {
    }

    public static Room toDomain(RoomEntity roomEntity) {
        return new Room(
            roomEntity.getId(),
            roomEntity.getTitle(),
            roomEntity.getDescription(),
            roomEntity.getPrice()
        );
    }

    public static RoomEntity toEntity(Room room) {
        return new RoomEntity(
                room.id(),
                room.title(),
                room.description(),
                room.price(),
                new ArrayList<>(),
                new ArrayList<>()
        );
    }
}
