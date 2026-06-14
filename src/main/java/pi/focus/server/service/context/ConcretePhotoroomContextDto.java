package pi.focus.server.service.context;

import pi.focus.server.api.context.IConcretePhotoroomContext;
import pi.focus.server.api.models.ITextCard;

import java.util.List;
import java.util.UUID;


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