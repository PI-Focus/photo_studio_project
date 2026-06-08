package pi.focus.server.api.context;

import java.util.List;

import pi.focus.server.api.models.IImagedTextCard;

public interface IEquipmentContext {
    List<IImagedTextCard> getEquipment();
}
