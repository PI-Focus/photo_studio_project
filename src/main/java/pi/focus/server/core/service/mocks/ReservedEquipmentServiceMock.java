package pi.focus.server.core.service.mocks;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pi.focus.server.core.service.api.IReservedEquipmentService;

/** Тестовая заглушка сервиса управления зарезервированным оборудованием. */
@Service
@Profile({"mock", "test"})
public class ReservedEquipmentServiceMock implements IReservedEquipmentService {
}
