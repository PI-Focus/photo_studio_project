package pi.focus.server.api.context;

import java.util.List;

import pi.focus.server.api.models.IImagedTextCard;

/**
 * Контекст страницы со списком доступного оборудование
 */
public interface IEquipmentContext {
    /** @return список карточек оборудования с изображением и описанием */
    List<IImagedTextCard> getEquipment();
}
