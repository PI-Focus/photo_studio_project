package pi.focus.server.core.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pi.focus.server.api.context.IPhotographersContext;
import pi.focus.server.api.models.IImagedTextCard;
import pi.focus.server.core.repository.PhotographerRepository;
import pi.focus.server.core.service.api.IPhotographerService;
import pi.focus.server.service.context.PhotographersContextDto;
import pi.focus.server.service.models.ImagedTextCardDto;

/**
 * Реализация сервиса для работы с фотографами студии.
 * Обеспечивает получение данных из БД и их подготовку для отображения на сайте.
 */
@Service
@Profile({"dev", "prod", "test"})
public class PhotographerService implements IPhotographerService {
    /** Репозиторий для доступа к данным фотографов */
    private final PhotographerRepository photographerRepository;

    /**
     * Конструктор для внедрения зависимости репозитория.
     * @param photographerRepository репозиторий фотографов
     */
    public PhotographerService(PhotographerRepository photographerRepository) {
        this.photographerRepository = photographerRepository;
    }

    /**
     * Получает список всех фотографов.
     * При маппинге имя и фамилия объединяются в одну строку.
     * 
     * @return контекст со списком карточек фотографов
     */
    @Override
    public IPhotographersContext getEquipmentContext() {
        return new PhotographersContextDto(
                photographerRepository.findAll().stream().map(photographerEntity ->
                        (IImagedTextCard) new ImagedTextCardDto(
                                photographerEntity.getName() + " " + photographerEntity.getSurname(),
                                photographerEntity.getDescription(),
                                photographerEntity.getPhotoPath()
                        )
                ).toList()
        );
    }
}
