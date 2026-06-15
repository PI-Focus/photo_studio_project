package pi.focus.server.api.models;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Интерфейс с краткой информацией о существующем бронировании.
 */
public interface IReservation {
    /** @return уникальный идентификатор бронирования */
    UUID getId();
    /** @return название забронированного фотозала */
    String getRoomTitle();
    /** @return дата и время начала */
    LocalDateTime getStart();
    /** @return дата и время окончания */
    LocalDateTime getEnd();
}
