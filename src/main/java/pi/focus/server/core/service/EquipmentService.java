package pi.focus.server.core.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pi.focus.server.api.context.IEquipmentContext;
import pi.focus.server.api.models.IImagedTextCard;
import pi.focus.server.core.repository.EquipmentRepository;

import pi.focus.server.core.service.api.IEquipmentService;
import pi.focus.server.service.context.EquipmentContextDto;
import pi.focus.server.service.models.ImagedTextCardDto;


@Service
@Profile({"dev", "prod"})
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
}
