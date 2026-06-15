package pi.focus.server.core.service.api;

import io.hypersistence.utils.hibernate.type.range.Range;
import pi.focus.server.api.context.IConcretePhotoroomContext;
import pi.focus.server.api.context.IPhotoroomsContext;
import pi.focus.server.api.models.ICalendar;
import pi.focus.server.core.domain.Room;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public interface IRoomService {
    IPhotoroomsContext getPhotoroomsContext();
    IConcretePhotoroomContext getConcretePhotoroomContext(UUID id);
    ICalendar getRoomCalendar(UUID id, LocalDate day);
    Boolean exists(UUID id);
    Room getRoomById(UUID id);
    Boolean freeRoom(UUID id, Range<LocalDateTime> time);
}