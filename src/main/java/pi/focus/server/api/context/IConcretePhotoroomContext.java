package pi.focus.server.api.context;

import java.util.List;
import java.util.UUID;

import pi.focus.server.api.models.ITextCard;

public interface IConcretePhotoroomContext {
    ITextCard getTextData();
    List<String> getImages();
    UUID getRoomUuid();
}
