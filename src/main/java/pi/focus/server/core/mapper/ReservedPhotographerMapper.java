package pi.focus.server.core.mapper;

import pi.focus.server.core.domain.ReservedPhotographer;
import pi.focus.server.core.entity.ReservedPhotographerEntity;

/**
 * Маппер для связей забронированных фотографов.
 * Выполняет конвертацию данных для сущности.
 */
public final class ReservedPhotographerMapper {
    private ReservedPhotographerMapper() {
    }

    /**
     * Преобразует сущность связи фотографа в доменную модель.
     * 
     * @param reservedPhotographerEntity сущность связи из БД
     * @return объект ReservedPhotographer
     */
    public static ReservedPhotographer toDomain(ReservedPhotographerEntity reservedPhotographerEntity) {
        return new ReservedPhotographer(
            reservedPhotographerEntity.getReservedPhotographerId(),
            reservedPhotographerEntity.getReservation().getId(),
            reservedPhotographerEntity.getPhotographer().getId()
        );
    }

    /**
     * Создает сущность связи фотографа для сохранения в БД.
     * 
     * @param reservedPhotographer доменная модель связи
     * @return сущность ReservedPhotographerEntity
     */
    public static ReservedPhotographerEntity toEntity(ReservedPhotographer reservedPhotographer) {
        return new ReservedPhotographerEntity(
            reservedPhotographer.reservedPhotographerId(),
            null,
            null
        );
    }
}
