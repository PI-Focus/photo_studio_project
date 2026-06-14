package pi.focus.server.core.mapper;


import pi.focus.server.core.domain.ReservedEquipment;
import pi.focus.server.core.entity.ReservedEquipmentEntity;


public final class ReservedEquipmentMapper {
    private ReservedEquipmentMapper() {
    }

    public static ReservedEquipment toDomain(ReservedEquipmentEntity reservedEquipmentEntity) {
        return new ReservedEquipment(
                reservedEquipmentEntity.getReservedEquipmentId(),
                reservedEquipmentEntity.getReservation().getId(),
                reservedEquipmentEntity.getEquipment().getId()
        );
    }

    public static ReservedEquipmentEntity toEntity(ReservedEquipment reservedEquipment) {
        return new ReservedEquipmentEntity(
                reservedEquipment.reservedEquipmentId(),
                null,
                null
        );
    }
}
