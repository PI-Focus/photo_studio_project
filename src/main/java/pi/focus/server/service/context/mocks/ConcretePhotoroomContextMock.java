package pi.focus.server.service.context.mocks;

import java.util.List;

import pi.focus.server.api.context.IConcretePhotoroomContext;
import pi.focus.server.api.models.ITextCard;
import pi.focus.server.service.models.TextCardDto;

@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class ConcretePhotoroomContextMock implements IConcretePhotoroomContext {
    private final ITextCard textData;
    private final List<String> images;

    public ConcretePhotoroomContextMock(String id) {
        textData = new TextCardDto(String.format("ЗAЛ %s", id), String.format("Хахахаха прикинь это зал %s. Очень крутой и название интересное))", id));
        images = List.of("/images/placeholder.png",
            "/images/placeholder.png",
            "/images/placeholder.png",
            "/images/placeholder.png"
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
