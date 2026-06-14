package pi.focus.server.core.service.api;

import pi.focus.server.api.context.IConcretePhotoroomContext;
import pi.focus.server.api.context.IPhotoroomsContext;
import pi.focus.server.api.models.ICalendar;

import java.time.LocalDate;
import java.util.UUID;

public interface IRoomService {
    IPhotoroomsContext getPhotoroomsContext();
    IConcretePhotoroomContext getConcretePhotoroomContext(UUID id);
    ICalendar getRoomCalendar(UUID id, LocalDate day);
}