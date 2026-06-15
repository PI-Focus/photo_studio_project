package pi.focus.server.core.service.mocks;

import io.hypersistence.utils.hibernate.type.range.Range;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pi.focus.server.api.context.IConcretePhotoroomContext;
import pi.focus.server.api.context.IPhotoroomsContext;
import pi.focus.server.api.models.ICalendar;
import pi.focus.server.core.domain.Room;
import pi.focus.server.core.service.api.IRoomService;
import pi.focus.server.service.models.mocks.CalendarMock;
import pi.focus.server.service.context.mocks.ConcretePhotoroomContextMock;
import pi.focus.server.service.context.mocks.PhotoroomsContextMock;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Мок-реализация сервиса фотозалов.
 * Предназначена для демонстрации интерфейса каталога залов без обращения к БД.
 */
@Service
@Profile({"mock", "test"})
public class RoomServiceMock implements IRoomService {
    @Override
    public IPhotoroomsContext getPhotoroomsContext() {
        return new PhotoroomsContextMock();
    }

    /**
     * Генерирует мок-данные для конкретного зала.
     * @param id UUID зала
     * @return контекст зала 
     */
    @Override
    public IConcretePhotoroomContext getConcretePhotoroomContext(UUID id) {
        return new ConcretePhotoroomContextMock(id);
    }

    @Override
    public ICalendar getRoomCalendar(UUID id, LocalDate day) {
        return new CalendarMock(day);
    }

    @Override
    public Boolean exists(UUID id) {
        return false;
    }

    @Override
    public Room getRoomById(UUID id) {
        return null;
    }

    @Override
    public Boolean freeRoom(UUID id, Range<LocalDateTime> time) {
        return false;
    }
}
