package pi.focus.server.core.service;

import io.hypersistence.utils.hibernate.type.range.Range;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pi.focus.server.api.context.IPhotographersContext;
import pi.focus.server.api.models.IImagedTextCard;
import pi.focus.server.core.domain.Photographer;
import pi.focus.server.core.entity.PhotographerEntity;
import pi.focus.server.core.entity.ReservationEntity;
import pi.focus.server.core.mapper.PhotographerMapper;
import pi.focus.server.core.repository.PhotographerRepository;
import pi.focus.server.core.service.api.IPhotographerService;
import pi.focus.server.service.context.PhotographersContextDto;
import pi.focus.server.service.models.ImagedTextCardDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


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

    @Override
    public List<Photographer> getPhotographersByTime(Range<LocalDateTime> time) {
        List<Photographer> photographers = new ArrayList<>();
        for (PhotographerEntity photographer: photographerRepository.findAll()) {
            if (freePhotographer(photographer.getId(), time)) {
                photographers.add(PhotographerMapper.toDomain(photographer));
            }
        }
        return photographers;
    }

    @Override
    public Boolean exists(UUID id) {
        return photographerRepository.findById(id).isPresent();
    }

    @Override
    public Photographer getPhotographerById(UUID id) {
        return photographerRepository.findById(id).map(PhotographerMapper::toDomain).orElse(null);
    }

    @Override
    public boolean freePhotographer(UUID id, Range<LocalDateTime> time) {
        Optional<PhotographerEntity> photographerOpt = photographerRepository.findById(id);
        if (photographerOpt.isEmpty()) {
            return false;
        }
        PhotographerEntity photographer = photographerOpt.get();
        boolean free = true;
        for (ReservationEntity reservation: photographer.getReservations()) {
            if (reservation.getTime().contains(time)) {
                free = false;
            }
        }
        return free;
    }
}
