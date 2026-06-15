package pi.focus.server.api.context;

import java.util.List;
import java.util.UUID;

import pi.focus.server.api.models.ITextCard;

/**
 * Контекст для страницы детальной информации о конкретной фотозале.
 */
public interface IConcretePhotoroomContext {
    /** @return текстовые данные зала */
    ITextCard getTextData();
    /** @return список ссылок на изображения */
    List<String> getImages();
    UUID getRoomUuid();
}
