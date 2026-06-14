package pi.focus.server.core.domain;

import java.util.UUID;

public record ReservedEquipment(
        UUID reservedEquipmentId,
        UUID reservationId,
        UUID equipmentId
) { }
