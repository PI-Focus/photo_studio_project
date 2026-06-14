package pi.focus.server.service.context;

import pi.focus.server.api.context.IPhotographersContext;
import pi.focus.server.api.models.IImagedTextCard;

import java.util.List;

public record PhotographersContextDto(List<IImagedTextCard> equipment) implements IPhotographersContext{
    @Override
    public List<IImagedTextCard> getPhotographers() {
        return equipment;
    }
}
