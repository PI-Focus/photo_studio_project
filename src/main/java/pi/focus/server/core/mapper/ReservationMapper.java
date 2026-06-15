package pi.focus.server.core.mapper;

import pi.focus.server.core.domain.Reservation;
import pi.focus.server.core.entity.ReservationEntity;

import java.util.ArrayList;

/**
 * Маппер для объектов бронирования.
 * Выполняет преобразование между сущностью и доменной моделью.
 */
public final class ReservationMapper {
    /** Приватный конструктор для предотвращения инстанцирования */
    private ReservationMapper() {
    }

    /**
     * Преобразует сущность бронирования в доменный объект.
     * Извлекает идентификаторы пользователя и зала из связанных сущностей.
     * 
     * @param reservationEntity исходная сущность из базы данных
     * @return заполненный объект бронирования Reservation
     */
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

    /**
     * Преобразует доменную модель бронирования в сущность для БД.
     * 
     * @param reservation исходный доменный объект
     * @return сущность ReservationEntity с пустыми списками доп. услуг
     */
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
