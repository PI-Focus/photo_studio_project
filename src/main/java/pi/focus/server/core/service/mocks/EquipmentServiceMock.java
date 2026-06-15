package pi.focus.server.core.service.mocks;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pi.focus.server.api.context.IEquipmentContext;
import pi.focus.server.core.service.api.IEquipmentService;
import pi.focus.server.service.context.mocks.EquipmentContextMock;

/**
 * Мок-реализация сервиса оборудования.
  * Предназначена для демонстрации интерфейса каталога оборудования без обращения к БД.
 */
@Service
@Profile({"mock", "test"})
public class EquipmentServiceMock implements IEquipmentService {
    /**
     * @return заполненный мок-контекст оборудования
     */
    @Override
    public IEquipmentContext getEquipmentContext() {
        return new EquipmentContextMock();
    }
}
