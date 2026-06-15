package pi.focus.server.api.models;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Интерфейс, содержащий детальную информацию о параметрах бронирования.
 */
public interface IOrder {
    /** @return время начала бронирования */
    LocalDateTime getStartTime();
    /** @return время окончания бронирования */
    LocalDateTime getEndTime();
    /** @return идентификатор выбранного фотографа (если есть) */
    UUID getPhotographerId();
    /** @return список дополнительного оборудования в заказе */
    List<IEquipment> getEquipment();
    /** @return итоговая стоимость заказа */
    Integer getPrice();

    void setStartTime(LocalDateTime startTime);
    void setEndTime(LocalDateTime endTime);
    void setPhotographerId(UUID photographerId);
    void setEquipment(List<IEquipment> equipment);
    void setPrice(Integer price);
}
