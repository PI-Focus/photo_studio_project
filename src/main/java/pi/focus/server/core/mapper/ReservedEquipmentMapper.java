package pi.focus.server.core.mapper;


import pi.focus.server.core.domain.ReservedEquipment;
import pi.focus.server.core.entity.ReservedEquipmentEntity;

/**
 * Маппер для связей забронированного оборудования.
 * Обрабатывает данные между сущностью-связкой и доменной моделью .
 */
public final class ReservedEquipmentMapper {
    private ReservedEquipmentMapper() {
    }

    /**
     * Конвертирует сущность связи в доменную модель.
     * 
     * @param reservedEquipmentEntity сущность связи из БД
     * @return объект ReservedEquipment с идентификаторами брони и оборудования
     */
    public static ReservedEquipment toDomain(ReservedEquipmentEntity reservedEquipmentEntity) {
        return new ReservedEquipment(
                reservedEquipmentEntity.getReservedEquipmentId(),
                reservedEquipmentEntity.getReservation().getId(),
                reservedEquipmentEntity.getEquipment().getId()
        );
    }

    /**
     * Создает сущность связи на основе доменной модели.
     * 
     * @param reservedEquipment доменная модель связи
     * @return сущность ReservedEquipmentEntity
     */
    public static ReservedEquipmentEntity toEntity(ReservedEquipment reservedEquipment) {
        return new ReservedEquipmentEntity(
                reservedEquipment.reservedEquipmentId(),
                null,
                null
        );
    }
}
