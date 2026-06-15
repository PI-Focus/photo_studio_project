package pi.focus.server.api.models;

import java.time.LocalDateTime;
import java.util.UUID;

public interface IReservation {
    UUID getId();
    String getRoomTitle();
    LocalDateTime getStart();
    LocalDateTime getEnd();
}
