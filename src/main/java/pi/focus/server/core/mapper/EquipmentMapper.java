package pi.focus.server.core.mapper;

import pi.focus.server.core.domain.Equipment;
import pi.focus.server.core.entity.EquipmentEntity;

import java.util.ArrayList;

/**
 * Маппер для преобразования объектов оборудования.
 * Выполняет конвертацию между сущностью БД  и доменной моделью.
 */
public final class EquipmentMapper {
    /** Закрытый конструктор */
    private EquipmentMapper() {
    }

    /**
     * Преобразует сущность базы данных в доменный объект оборудования.
     * 
     * @param equipmentEntity исходная сущность из БД
     * @return заполненная доменная модель Equipment
     */
    public static Equipment toDomain(EquipmentEntity equipmentEntity) {
        return new Equipment(
                equipmentEntity.getId(),
                equipmentEntity.getTitle(),
                equipmentEntity.getDescription(),
                equipmentEntity.getPrice(),
                equipmentEntity.getPhotoPath()
        );
    }

    /**
     * Преобразует доменную модель оборудования в сущность для сохранения в БД.
     * 
     * @param equipment исходный доменный объект
     * @return сущность
     */
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
