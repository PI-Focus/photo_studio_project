package pi.focus.server.core.service.mocks;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pi.focus.server.api.context.IEquipmentContext;
import pi.focus.server.core.domain.Equipment;
import pi.focus.server.core.service.api.IEquipmentService;
import pi.focus.server.service.context.mocks.EquipmentContextMock;

import java.util.List;
import java.util.UUID;

@Service
@Profile({"mock", "test"})
public class EquipmentServiceMock implements IEquipmentService {
    @Override
    public IEquipmentContext getEquipmentContext() {
        return new EquipmentContextMock();
    }

    @Override
    public List<Equipment> getEquipment() {
        return List.of();
    }

    @Override
    public Boolean exists(UUID id) {
        return false;
    }

    @Override
    public Equipment getEquipmentById(UUID id) {
        return null;
    }
}
