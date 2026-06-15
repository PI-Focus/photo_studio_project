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

/**
 * Сервис для управления данными фотозалов.
 * Реализует логику сборки контекстов для отображения каталога залов и страниц конкретных локаций.
 */
@Service
@Profile({"dev", "prod", "test"})
public class RoomService implements IRoomService {
    /** Путь к заглушке изображения, если у зала нет загруженных фото */
    @Value("${app.static-data.placeholder-path}")
    private String placeholderPath;
    private final RoomRepository roomRepository;

    /**
     * Конструктор для внедрения зависимости репозитория.
     * @param roomRepository репозиторий залов
     */
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    /**
     * Формирует список карточек всех залов для страницы каталога.
     * Для каждой карточки выбирается первое доступное фото из галереи зала.
     * 
     * @return контекст со списком залов для фронтенда
     */
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

    /**
     * Получает детальную информацию о зале по его ID.
     * 
     * @param id уникальный идентификатор зала
     * @return контекст конкретного зала или null, если зал не найден
     */
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
