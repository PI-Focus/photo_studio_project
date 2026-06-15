package pi.focus.server.core.service.api;

import io.hypersistence.utils.hibernate.type.range.Range;
import pi.focus.server.api.context.IPhotographersContext;
import pi.focus.server.core.domain.Photographer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Интерфейс сервиса для работы с командой фотографов студии.
 */
public interface IPhotographerService {
    /**
     * Возвращает данные о фотографах для формирования страницы каталога.
     * @return контекст со списком фотографов
     */
    IPhotographersContext getEquipmentContext();
    List<Photographer> getPhotographersByTime(Range<LocalDateTime> time);
    Boolean exists(UUID id);
    Photographer getPhotographerById(UUID id);
    boolean freePhotographer(UUID id, Range<LocalDateTime> time);
}
