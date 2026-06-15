package pi.focus.server.core.mapper;

import pi.focus.server.core.domain.Room;
import pi.focus.server.core.entity.RoomEntity;

import java.util.ArrayList;

/**
 * Маппер для работы с данными фотозалов.
 * Связывает сущность и доменную модель .
 */
public final class RoomMapper {
    private RoomMapper() {
    }

    /**
     * Переводит данные пользователя из БД в слой домена.
     * 
     * @param roomEntity сущность зала
     * @return доменный объект Room
     */
    public static Room toDomain(RoomEntity roomEntity) {
        return new Room(
            roomEntity.getId(),
            roomEntity.getTitle(),
            roomEntity.getDescription(),
            roomEntity.getPrice()
        );
    }

    /**
     * Переводит данные пользователя из доменного слоя в сущность БД.
     * 
     * @param room доменный объект зала
     * @return сущность RoomEntity с пустым списком бронирований
     */
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
