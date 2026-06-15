package pi.focus.server.api.models;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface IOrder {
    LocalDateTime getStartTime();
    LocalDateTime getEndTime();
    UUID getPhotographerId();
    List<IEquipment> getEquipment();
    Integer getPrice();

    void setStartTime(LocalDateTime startTime);
    void setEndTime(LocalDateTime endTime);
    void setPhotographerId(UUID photographerId);
    void setEquipment(List<IEquipment> equipment);
    void setPrice(Integer price);
}
