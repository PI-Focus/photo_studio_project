package pi.focus.server.api.models;

import pi.focus.server.service.models.EquipmentDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface IOrder {
    LocalDateTime getStartTime();
    LocalDateTime getEndTime();
    UUID getPhotographerId();
    List<EquipmentDto> getEquipment();
    Integer getPrice();

    void setStartTime(LocalDateTime startTime);
    void setEndTime(LocalDateTime endTime);
    void setPhotographerId(UUID photographerId);
    void setEquipment(List<EquipmentDto> equipment);
    void setPrice(Integer price);
}
