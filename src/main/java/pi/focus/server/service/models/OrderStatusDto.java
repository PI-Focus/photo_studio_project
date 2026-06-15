package pi.focus.server.service.models;

import pi.focus.server.api.models.IOrder;
import pi.focus.server.api.models.IOrderStatus;
<<<<<<< HEAD
import tools.jackson.databind.annotation.JsonDeserialize;
=======
>>>>>>> feature/booking

import java.io.Serializable;
import java.util.UUID;

public class OrderStatusDto implements IOrderStatus, Serializable {
    private static final long serialVersionUID = 1L;
    private UUID roomId;
<<<<<<< HEAD
    @JsonDeserialize(as = OrderDto.class)
=======
>>>>>>> feature/booking
    private IOrder body;

    public OrderStatusDto(UUID roomId, OrderDto body) {
        this.roomId = roomId;
        this.body = body;
    }

    @Override
    public UUID getRoomId() {
        return roomId;
    }

    @Override
    public IOrder getBody() {
        return body;
    }

    @Override
    public void setRoomId(UUID roomId) {
        this.roomId = roomId;
    }

    @Override
    public void setBody(IOrder body) {
        this.body = body;
    }

}
