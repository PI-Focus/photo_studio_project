package pi.focus.server.core.mapper;

import pi.focus.server.core.domain.Equipment;
import pi.focus.server.core.entity.EquipmentEntity;

import java.util.ArrayList;


public final class EquipmentMapper {
    private EquipmentMapper() {
    }

    public static Equipment toDomain(EquipmentEntity equipmentEntity) {
        return new Equipment(
                equipmentEntity.getId(),
                equipmentEntity.getTitle(),
                equipmentEntity.getDescription(),
                equipmentEntity.getPrice(),
                equipmentEntity.getPhotoPath()
        );
    }

    public static EquipmentEntity toEntity(Equipment equipment) {
        return new EquipmentEntity(
                equipment.id(),
                equipment.title(),
                equipment.description(),
                equipment.price(),
                equipment.photoPath(),
                new ArrayList<>()
        );
    }
}
