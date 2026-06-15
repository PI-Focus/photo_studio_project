package pi.focus.server.core.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pi.focus.server.api.context.IEquipmentContext;
import pi.focus.server.api.models.IImagedTextCard;
import pi.focus.server.core.repository.EquipmentRepository;

import pi.focus.server.core.service.api.IEquipmentService;
import pi.focus.server.service.context.EquipmentContextDto;
import pi.focus.server.service.models.ImagedTextCardDto;

/**
 * Реализация сервиса управления оборудованием.
 * Работает с реальными данными из базы данных. Активен в профилях dev, prod и test.
 */
@Service
@Profile({"dev", "prod", "test"})
public class EquipmentService implements IEquipmentService {
    /** Репозиторий для доступа к таблице оборудования */
    private final EquipmentRepository equipmentRepository;

    /**
     * Конструктор для внедрения зависимости репозитория.
     * @param equipmentRepository репозиторий оборудования
     */
    public EquipmentService(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    /**
     * Извлекает всё оборудование из базы данных и преобразует его в формат контекста для фронтенда.
     * 
     * @return объект IEquipmentContext, содержащий список карточек оборудования
     */
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
