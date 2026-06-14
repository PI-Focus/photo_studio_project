package pi.focus.server.core.service.api;


import pi.focus.server.api.context.IEquipmentContext;
import pi.focus.server.core.domain.Equipment;

import java.util.List;

public interface IEquipmentService {
    IEquipmentContext getEquipmentContext();
    List<Equipment> getEquipment();
}
