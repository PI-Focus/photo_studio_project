package pi.focus.server.api.models;


import java.util.UUID;

/**
 * Интерфейс, описывающий единицу оборудования в контексте заказа.
 */
public interface IEquipment {
    /** @return уникальный идентификатор оборудования */
    UUID getId();
    /** @return количество забронированных единиц */
    Integer getCount();

    void setId(UUID id);
    void setCount(Integer count);
}
