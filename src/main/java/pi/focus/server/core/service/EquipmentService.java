package pi.focus.server.core.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pi.focus.server.api.context.IEquipmentContext;
import pi.focus.server.api.models.IImagedTextCard;
import pi.focus.server.core.domain.Equipment;
import pi.focus.server.core.mapper.EquipmentMapper;
import pi.focus.server.core.repository.EquipmentRepository;

import pi.focus.server.core.service.api.IEquipmentService;
import pi.focus.server.service.context.EquipmentContextDto;
import pi.focus.server.service.models.ImagedTextCardDto;

import java.util.List;
import java.util.UUID;


@Service
@Profile({"dev", "prod", "test"})
public class EquipmentService implements IEquipmentService {
    private final EquipmentRepository equipmentRepository;

    public EquipmentService(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    @Override
    public IEquipmentContext getEquipmentContext() {
        return new EquipmentContextDto(
               equipmentRepository.findAll().stream().map(equipmentEntity ->
                       (IImagedTextCard) new ImagedTextCardDto(
                               equipmentEntity.getTitle(),
                               equipmentEntity.getDescription(),
                               equipmentEntity.getPhotoPath()
                       )
               ).toList()
        );
    }

    @Override
    public List<Equipment> getEquipment() {
        return equipmentRepository.findAll().stream().map(EquipmentMapper::toDomain).toList();
    }

    @Override
    public Boolean exists(UUID id) {
        return equipmentRepository.findById(id).isPresent();
    }

    @Override
    public Equipment getEquipmentById(UUID id) {
        return equipmentRepository.findById(id).map(EquipmentMapper::toDomain).orElse(null);
    }
}
