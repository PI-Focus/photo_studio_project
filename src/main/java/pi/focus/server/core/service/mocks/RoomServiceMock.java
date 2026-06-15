package pi.focus.server.core.service.mocks;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pi.focus.server.api.context.IConcretePhotoroomContext;
import pi.focus.server.api.context.IPhotoroomsContext;
import pi.focus.server.core.service.api.IRoomService;
import pi.focus.server.service.context.mocks.ConcretePhotoroomContextMock;
import pi.focus.server.service.context.mocks.PhotoroomsContextMock;

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
        return new ConcretePhotoroomContextMock(id.toString().substring(0, 8));
    }
}
