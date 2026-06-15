package pi.focus.server.service.models;

import pi.focus.server.api.models.IOrder;
import pi.focus.server.api.models.IOrderStatus;
import tools.jackson.databind.annotation.JsonDeserialize;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO верхнего уровня для процесса бронирования.
 * Связывает идентификатор комнаты с телом заказа.
 */
public class OrderStatusDto implements IOrderStatus, Serializable {
    private static final long serialVersionUID = 1L;
    private UUID roomId;
    /** Детали заказа с явным указанием реализации для Jackson */
    @JsonDeserialize(as = OrderDto.class)
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
