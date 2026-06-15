package pi.focus.server.service.context;

import pi.focus.server.api.context.IPhotoroomsContext;
import pi.focus.server.api.models.IDataCard;

import java.util.List;

/**
 * DTO для передачи списка залов в общий каталог.
 * 
 * @param photorooms список карточек залов
 */
public record PhotoroomsContextDto(List<IDataCard> photorooms) implements IPhotoroomsContext {
    @Override
    public List<IDataCard> getPhotorooms() {
        return photorooms;
    }
}
