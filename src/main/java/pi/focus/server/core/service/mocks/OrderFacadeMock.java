package pi.focus.server.core.service.mocks;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pi.focus.server.api.models.IOrderStatus;
import pi.focus.server.core.service.api.IOrderFacade;

import java.util.UUID;

/**
 * Заглушка фасада управления заказами.
 * Используется в тестовом окружении или при профиле "mock" для изоляции от базы данных и реальной логики.
 */
@Service
@Profile({"mock", "test"})
public class OrderFacadeMock implements IOrderFacade {
    /** @return всегда возвращает null в mock-реализации */
    @Override
    public IOrderStatus getEmptyOrderStatus() {
        return null;
    }

    /** @return всегда возвращает -1 (ошибка) в mock-реализации */
    @Override
    public Integer validateOrderStatus(IOrderStatus orderStatus) {
        return -1;
    }

    /** Пустая реализация метода создания бронирования */
    @Override
    public void createReservation(UUID id, IOrderStatus orderStatus) {
    }
}
