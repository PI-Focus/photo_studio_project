package pi.focus.server.service.context;

import pi.focus.server.api.context.IConcretePhotoroomContext;
import pi.focus.server.api.models.ITextCard;

import java.util.List;
import java.util.UUID;

/**
 * DTO для передачи данных о конкретном фотозале.
 * 
 * @param textData текстовое описание зала (заголовок и контент)
 * @param images список путей к изображениям для галереи зала
 */
public record ConcretePhotoroomContextDto(
        ITextCard textData,
        List<String> images,
        UUID roomUuid
) implements IConcretePhotoroomContext {
    @Override
    public ITextCard getTextData() {
        return textData;
    }

    @Override
    public List<String> getImages() {
        return images;
    }

    @Override
    public UUID getRoomUuid() {
        return roomUuid;
    }
}