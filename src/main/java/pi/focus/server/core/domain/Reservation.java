package pi.focus.server.core.domain;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Данные о бронировании зала.
 * @param id уникальный идентификатор брони
 * @param userId идентификатор пользователя, сделавшего заказ
 * @param roomId идентификатор забронированного зала
 * @param day дата бронирования
 * @param fromTime время начала (в часах от 0 до 23)
 * @param toTime время окончания (в часах от 1 до 24)
 */
public record Reservation(
        UUID id ,
        UUID userId,
        UUID roomId,
        LocalDate day,
        Short fromTime,
        Short toTime
) { }
