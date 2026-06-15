package pi.focus.server.service.models;

<<<<<<< HEAD
import pi.focus.server.api.models.IEquipment;
import pi.focus.server.api.models.IOrder;
import tools.jackson.databind.annotation.JsonDeserialize;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
=======
import pi.focus.server.api.models.IOrder;

import java.io.Serializable;
import java.time.LocalDateTime;
>>>>>>> feature/booking
import java.util.List;
import java.util.UUID;

public class OrderDto implements IOrder, Serializable {
    private static final long serialVersionUID = 1L;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private UUID photographerId;
<<<<<<< HEAD
    @JsonDeserialize(as = ArrayList.class, contentAs = EquipmentDto.class)
    private List<IEquipment> equipment;
=======
    private List<EquipmentDto> equipment;
>>>>>>> feature/booking
    private Integer price;

    public OrderDto(
            LocalDateTime startTime,
            LocalDateTime endTime,
            UUID photographerId,
<<<<<<< HEAD
            List<IEquipment> equipment,
=======
            List<EquipmentDto> equipment,
>>>>>>> feature/booking
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
<<<<<<< HEAD
    public List<IEquipment> getEquipment() {
=======
    public List<EquipmentDto> getEquipment() {
>>>>>>> feature/booking
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
<<<<<<< HEAD
    public void setEquipment(List<IEquipment> equipment) {
=======
    public void setEquipment(List<EquipmentDto> equipment) {
>>>>>>> feature/booking
        this.equipment = equipment;
    }

    @Override
    public void setPrice(Integer price) {
        this.price = price;
    }
}
