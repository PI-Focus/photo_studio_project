package pi.focus.server.api.models;

import pi.focus.server.service.models.OrderStatusDto;
import tools.jackson.databind.annotation.JsonDeserialize;


import java.util.UUID;

@JsonDeserialize(as = OrderStatusDto.class)
public interface IOrderStatus {
    UUID getRoomId();
    IOrder getBody();

    void setRoomId(UUID roomId);
    void setBody(IOrder body);
}
