package pi.focus.server.core.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pi.focus.server.api.context.IPhotographersContext;
import pi.focus.server.api.models.IImagedTextCard;
import pi.focus.server.core.repository.PhotographerRepository;
import pi.focus.server.core.service.api.IPhotographerService;
import pi.focus.server.service.context.PhotographersContextDto;
import pi.focus.server.service.models.ImagedTextCardDto;

@Service
@Profile({"dev", "prod", "test"})
public class PhotographerService implements IPhotographerService {
    private final PhotographerRepository photographerRepository;

    public PhotographerService(PhotographerRepository photographerRepository) {
        this.photographerRepository = photographerRepository;
    }

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
