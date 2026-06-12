package pi.focus.server.core.mapper;

import pi.focus.server.core.domain.Room;
import pi.focus.server.core.entity.RoomEntity;


public final class RoomMapper {
    private RoomMapper() {
    }

    public static Room toDomain(RoomEntity roomEntity) {
        return new Room(
            roomEntity.getId(),
            roomEntity.getTitle(),
            roomEntity.getDescription()
        );
    }

    public static RoomEntity toEntity(Room room) {
        return new RoomEntity(
                room.id(),
                room.title(),
                room.description()
        );
    }
}
