package pi.focus.server.core.mapper;

import pi.focus.server.core.domain.Reservation;
import pi.focus.server.core.entity.ReservationEntity;

import java.util.ArrayList;

public final class ReservationMapper {
    private ReservationMapper() {
    }

    public static Reservation toDomain(ReservationEntity reservationEntity) {
        return new Reservation(
                reservationEntity.getId(),
                reservationEntity.getUser().getId(),
                reservationEntity.getRoom().getId(),
                reservationEntity.getTime()
        );
    }

    public static ReservationEntity toEntity(Reservation reservation) {
        return new ReservationEntity(
                reservation.id(),
                null,
                null,
                reservation.time(),
                new ArrayList<>(),
                new ArrayList<>()
        );
    }
}
