package pi.focus.server.api.models;

import java.util.UUID;

public interface IOrderStatus {
    UUID getRoomId();
    IOrder getBody();

    void setRoomId(UUID roomId);
    void setBody(IOrder body);
}
