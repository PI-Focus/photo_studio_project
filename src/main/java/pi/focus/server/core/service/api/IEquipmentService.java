package pi.focus.server.core.service.api;


import pi.focus.server.api.context.IEquipmentContext;
import pi.focus.server.core.domain.Equipment;

import java.util.List;
import java.util.UUID;

public interface IEquipmentService {
    IEquipmentContext getEquipmentContext();
    List<Equipment> getEquipment();
    Boolean exists(UUID id);
}
