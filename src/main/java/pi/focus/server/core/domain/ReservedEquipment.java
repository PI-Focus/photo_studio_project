package pi.focus.server.core.domain;

import java.util.UUID;

/**
 * Связь забронированного оборудования с конкретным заказом.
 * @param reservedEquipmentId ID записи
 * @param reservationId ID бронирования
 * @param equipmentId ID оборудования
 */
public record ReservedEquipment(
        UUID reservedEquipmentId,
        UUID reservationId,
        UUID equipmentId,
        Integer count
) { }
