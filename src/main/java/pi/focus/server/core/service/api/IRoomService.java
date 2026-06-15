package pi.focus.server.core.service.api;

import io.hypersistence.utils.hibernate.type.range.Range;
import pi.focus.server.api.context.IConcretePhotoroomContext;
import pi.focus.server.api.context.IPhotoroomsContext;
import pi.focus.server.api.models.ICalendar;
import pi.focus.server.core.domain.Room;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Интерфейс сервиса для управления данными фотозалов.
 */
public interface IRoomService {
    /**
     * Возвращает краткую информацию о всех фотозалах для страницы каталога.
     * @return контекст списка залов
     */
    IPhotoroomsContext getPhotoroomsContext();
    /**
     * Возвращает детальную информацию о конкретном зале по его идентификатору.
     * @param id уникальный идентификатор зала
     * @return контекст конкретного зала с описанием и галереей
     */
    IConcretePhotoroomContext getConcretePhotoroomContext(UUID id);
    ICalendar getRoomCalendar(UUID id, LocalDate day);
    Boolean exists(UUID id);
    Room getRoomById(UUID id);
    Boolean freeRoom(UUID id, Range<LocalDateTime> time);
}