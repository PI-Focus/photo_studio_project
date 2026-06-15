package pi.focus.server.service.context;

import pi.focus.server.api.context.IEquipmentContext;
import pi.focus.server.api.models.IImagedTextCard;

import java.util.List;

/**
 * DTO для контекста страницы оборудования.
 * 
 * @param equipment список карточек доступного оборудования
 */
public record EquipmentContextDto(List<IImagedTextCard> equipment) implements IEquipmentContext{
    @Override
    public List<IImagedTextCard> getEquipment() {
        return equipment;
    }
}
