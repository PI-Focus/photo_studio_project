package pi.focus.server.service.models;

import pi.focus.server.api.models.IEquipment;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO единицы оборудования, используемое в составе заказа.
 */
public class EquipmentDto implements IEquipment, Serializable {
    private static final long serialVersionUID = 1L;
    private UUID id;
    private Integer count;

    public EquipmentDto(UUID id, Integer count) {
        this.id = id;
        this.count = count;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public Integer getCount() {
        return count;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public void setCount(Integer count) {
        this.count = count;
    }
}
