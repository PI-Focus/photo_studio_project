package pi.focus.server.api.context;

import java.util.List;

import pi.focus.server.api.models.IDataCard;

/** 
 * Контекст страницы списка всех фотозалов
 */
public interface IPhotoroomsContext {
    /** @return список карточек залов для общего каталога */
    List<IDataCard> getPhotorooms();
}
