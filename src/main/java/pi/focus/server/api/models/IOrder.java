package pi.focus.server.api.models;

import pi.focus.server.service.models.OrderDto;
import tools.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@JsonDeserialize(as = OrderDto.class)
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
