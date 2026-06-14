package pi.focus.server.api.context;

import java.util.List;

import pi.focus.server.api.models.ITextCard;

public interface IConcretePhotoroomContext {
    ITextCard getTextData();
    List<String> getImages();
}
