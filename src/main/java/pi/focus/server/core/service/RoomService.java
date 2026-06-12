package pi.focus.server.core.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pi.focus.server.api.context.IPhotoroomsContext;
import pi.focus.server.api.models.IDataCard;
import pi.focus.server.core.entity.RoomEntity;
import pi.focus.server.core.repository.RoomRepository;
import pi.focus.server.core.service.api.IRoomService;
import pi.focus.server.service.context.PhotoroomsContextDto;
import pi.focus.server.service.models.DataCardDto;

import java.util.ArrayList;
import java.util.List;


@Service
@Profile({"dev", "prod"})
public class RoomService implements IRoomService {
    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public IPhotoroomsContext getPhotoroomsContext() {
        List<RoomEntity> roomEntities = roomRepository.findAll();
        List<IDataCard> dataCards = new ArrayList<>();
        for (RoomEntity roomEntity: roomEntities) {
            dataCards.add(
                    new DataCardDto(
                            roomEntity.getTitle(),
                            roomEntity.getDescription(),
                            roomEntity.getPhotos().getFirst().getPath(),
                            "photorooms/" + roomEntity.getId()
                    )
            );
        }
        return new PhotoroomsContextDto(dataCards);
    }
}
