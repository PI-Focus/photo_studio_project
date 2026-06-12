package pi.focus.server.service.context;

import pi.focus.server.api.context.IEquipmentContext;
import pi.focus.server.api.models.IImagedTextCard;

import java.util.List;

public record EquipmentContextDto(List<IImagedTextCard> equipment) implements IEquipmentContext{
    @Override
    public List<IImagedTextCard> getEquipment() {
        return equipment;
    }
}
