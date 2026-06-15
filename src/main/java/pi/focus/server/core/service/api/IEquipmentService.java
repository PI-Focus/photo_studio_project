package pi.focus.server.core.service.api;


import pi.focus.server.api.context.IEquipmentContext;
import pi.focus.server.core.domain.Equipment;

import java.util.List;
import java.util.UUID;

/**
 * Интерфейс сервиса для управления каталогом оборудования.
 */
public interface IEquipmentService {
    /**
     * Возвращает данные о доступном оборудовании для отображения в пользовательском интерфейсе.
     * @return контекст со списком оборудования
     */
    IEquipmentContext getEquipmentContext();
    List<Equipment> getEquipment();
    Boolean exists(UUID id);
    Equipment getEquipmentById(UUID id);
}
