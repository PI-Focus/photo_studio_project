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
                reservationEntity.getDay(),
                reservationEntity.getFromTime(),
                reservationEntity.getToTime()
        );
    }

    public static ReservationEntity toEntity(Reservation reservation) {
        return new ReservationEntity(
                reservation.id(),
                null,
                null,
                reservation.day(),
                reservation.fromTime(),
                reservation.toTime(),
                new ArrayList<>(),
                new ArrayList<>()
        );
    }
}
