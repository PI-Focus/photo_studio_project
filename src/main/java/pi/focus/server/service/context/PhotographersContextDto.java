package pi.focus.server.service.context;

import pi.focus.server.api.context.IPhotographersContext;
import pi.focus.server.api.models.IImagedTextCard;

import java.util.List;

/**
 * DTO для контекста страницы фотографов.
 * 
 * @param equipment список карточек фотографов (используется общий интерфейс карточки с картинкой)
 */
public record PhotographersContextDto(List<IImagedTextCard> equipment) implements IPhotographersContext{
    @Override
    public List<IImagedTextCard> getPhotographers() {
        return equipment;
    }
}
