package pi.focus.server.service.models;

import pi.focus.server.api.models.IEquipment;
import pi.focus.server.api.models.IOrder;
import tools.jackson.databind.annotation.JsonDeserialize;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * DTO с подробной информацией о параметрах заказа: время, фотограф, оборудование и цена.
 */
public class OrderDto implements IOrder, Serializable {
    private static final long serialVersionUID = 1L;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private UUID photographerId;
    /** Список оборудования с указанием конкретного класса реализации для десериализации */
    @JsonDeserialize(as = ArrayList.class, contentAs = EquipmentDto.class)
    private List<IEquipment> equipment;
    private Integer price;

    public OrderDto(
            LocalDateTime startTime,
            LocalDateTime endTime,
            UUID photographerId,
            List<IEquipment> equipment,
            Integer price
    ) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.photographerId = photographerId;
        this.equipment = equipment;
        this.price = price;
    }

    @Override
    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public UUID getPhotographerId() {
        return photographerId;
    }

    @Override
    public List<IEquipment> getEquipment() {
        return equipment;
    }

    @Override
    public Integer getPrice() {
        return price;
    }

    @Override
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public void setPhotographerId(UUID photographerId) {
        this.photographerId = photographerId;
    }

    @Override
    public void setEquipment(List<IEquipment> equipment) {
        this.equipment = equipment;
    }

    @Override
    public void setPrice(Integer price) {
        this.price = price;
    }
}
