package pi.focus.server.core.mapper;

import pi.focus.server.core.domain.ReservedPhotographer;
import pi.focus.server.core.entity.ReservedPhotographerEntity;

public final class ReservedPhotographerMapper {
    private ReservedPhotographerMapper() {
    }

    public static ReservedPhotographer toDomain(ReservedPhotographerEntity reservedPhotographerEntity) {
        return new ReservedPhotographer(
            reservedPhotographerEntity.getReservedPhotographerId(),
            reservedPhotographerEntity.getReservation().getId(),
            reservedPhotographerEntity.getPhotographer().getId()
        );
    }

    public static ReservedPhotographerEntity toEntity(ReservedPhotographer reservedPhotographer) {
        return new ReservedPhotographerEntity(
            reservedPhotographer.reservedPhotographerId(),
            null,
            null
        );
    }
}
