package pi.focus.server.core.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pi.focus.server.api.context.IConcretePhotoroomContext;
import pi.focus.server.api.context.IPhotoroomsContext;
import pi.focus.server.api.models.IDataCard;
import pi.focus.server.core.entity.PhotoEntity;
import pi.focus.server.core.entity.RoomEntity;
import pi.focus.server.core.repository.RoomRepository;
import pi.focus.server.core.service.api.IRoomService;
import pi.focus.server.service.context.ConcretePhotoroomContextDto;
import pi.focus.server.service.context.PhotoroomsContextDto;
import pi.focus.server.service.models.DataCardDto;
import pi.focus.server.service.models.TextCardDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@Profile({"dev", "prod", "test"})
public class RoomService implements IRoomService {
    @Value("${app.static-data.placeholder-path}")
    private String placeholderPath;
    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public IPhotoroomsContext getPhotoroomsContext() {
        List<RoomEntity> roomEntities = roomRepository.findAll();
        List<IDataCard> dataCards = new ArrayList<>();
        String photoPath = placeholderPath;
        for (RoomEntity roomEntity: roomEntities) {
            List<PhotoEntity> photos = roomEntity.getPhotos();
            if (!photos.isEmpty()) {
                photoPath = photos.getFirst().getPath();
            }
            dataCards.add(
                    new DataCardDto(
                            roomEntity.getTitle(),
                            roomEntity.getDescription(),
                            photoPath,
                            "photorooms/" + roomEntity.getId()
                    )
            );
        }
        return new PhotoroomsContextDto(dataCards);
    }

    @Override
    public IConcretePhotoroomContext getConcretePhotoroomContext(UUID id) {
        Optional<RoomEntity> roomOpt = roomRepository.findById(id);
        if (roomOpt.isEmpty()) {
            return null;
        }
        RoomEntity roomEntity = roomOpt.get();
        return new ConcretePhotoroomContextDto(
                new TextCardDto(
                        "Зал " + roomEntity.getTitle(),
                        roomEntity.getDescription()
                ),
                roomEntity.getPhotos().stream().map(PhotoEntity::getPath).toList()
        );
    }
}
