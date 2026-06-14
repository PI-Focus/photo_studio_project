package pi.focus.server.api.models;

import java.util.UUID;

public interface IEquipment {
    UUID getId();
    Integer getCount();

    void setId(UUID id);
    void setCount(Integer count);
}
