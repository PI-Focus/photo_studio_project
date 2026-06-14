package pi.focus.server.service.context;

import pi.focus.server.api.context.IConcretePhotoroomContext;
import pi.focus.server.api.models.ITextCard;

import java.util.List;


public record ConcretePhotoroomContextDto(
        ITextCard textData,
        List<String> images
) implements IConcretePhotoroomContext {
    @Override
    public ITextCard getTextData() {
        return textData;
    }

    @Override
    public List<String> getImages() {
        return images;
    }
}