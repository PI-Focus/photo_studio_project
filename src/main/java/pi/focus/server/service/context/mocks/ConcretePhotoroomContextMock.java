package pi.focus.server.service.context.mocks;

import java.util.List;
import java.util.UUID;

import pi.focus.server.api.context.IConcretePhotoroomContext;
import pi.focus.server.api.models.ITextCard;
import pi.focus.server.service.models.TextCardDto;

@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class ConcretePhotoroomContextMock implements IConcretePhotoroomContext {
    private final ITextCard textData;
    private final List<String> images;
    private final UUID roomUuid;

    public ConcretePhotoroomContextMock(UUID id) {
        String name = id.toString().substring(0, 8);
        textData = new TextCardDto(String.format("ЗAЛ %s", name), String.format("Хахахаха прикинь это зал %s. Очень крутой и название интересное))", name));
        images = List.of("/images/placeholder.png",
            "/images/placeholder.png",
            "/images/placeholder.png",
            "/images/placeholder.png"
        );
        roomUuid = id;
    }

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
