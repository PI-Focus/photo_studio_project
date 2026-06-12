package pi.focus.server.service.context.mocks;

import java.util.List;

import pi.focus.server.api.context.IConcretePhotoroomContext;
import pi.focus.server.api.models.ITextCard;
import pi.focus.server.service.models.TextCardDto;

public class ConcretePhotoroomContextMock implements IConcretePhotoroomContext {
    private final ITextCard textData;
    private final List<String> images;

    public ConcretePhotoroomContextMock(String id) {
        textData = new TextCardDto(String.format("Зал %s", id), String.format("Хахахаха прикинь это зад %s. Очень крутой и название интересное))", id));
        images = List.of("/imges/placeholder.png",
            "/imges/placeholder.png",
            "/imges/placeholder.png",
            "/imges/placeholder.png"
        );
    }

    @Override
    public ITextCard getTextData() {
        return textData;
    }

    @Override
    public List<String> getImages() {
        return images;
    }
}
