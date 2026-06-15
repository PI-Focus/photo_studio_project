package pi.focus.server.api.context;

import java.util.List;

import pi.focus.server.api.models.IImagedTextCard;

/**
 * Контекст страницы списка фотографов.
 */
public interface IPhotographersContext {
    /** @return список карточек фотографов для общего каталога */
    List<IImagedTextCard> getPhotographers();
}
