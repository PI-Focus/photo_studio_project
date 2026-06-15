package pi.focus.server.core.service.mocks;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pi.focus.server.api.context.IPhotographersContext;
import pi.focus.server.core.service.api.IPhotographerService;
import pi.focus.server.service.context.mocks.PhotographersContextMock;

/**
 * Мок-реализация сервиса фотографов.
 * Предназначена для демонстрации интерфейса каталога фотографов без обращения к БД.
 */
@Service
@Profile({"mock", "test"})
public class PhotographerServiceMock implements IPhotographerService {
    /**
     * @return заполненный мок-контекст фотографов
     */
    @Override
    public IPhotographersContext getEquipmentContext() {
        return new PhotographersContextMock();
    }       
}
