package pi.focus.server.service.models;

import pi.focus.server.api.models.IReservation;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO с краткой информацией о совершенном бронировании.
 */
public record ReservationDto(
        UUID id,
        String roomTitle,
        LocalDateTime start,
        LocalDateTime end
) implements IReservation {
    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public String getRoomTitle() {
        return roomTitle;
    }

    @Override
    public LocalDateTime getStart() {
        return start;
    }

    @Override
    public LocalDateTime getEnd() {
        return end;
    }
}
