package pi.focus.server.api.models;

import pi.focus.server.service.models.EquipmentDto;
import tools.jackson.databind.annotation.JsonDeserialize;

import java.util.UUID;

@JsonDeserialize(as = EquipmentDto.class)
public interface IEquipment {
    UUID getId();
    Integer getCount();

    void setId(UUID id);
    void setCount(Integer count);
}
