package pi.focus.server.api.models;


import java.util.UUID;

/**
 * Интерфейс для отслеживания текущего состояния формируемого заказа.
 * Связывает конкретный зал с параметрами бронирования.
 */
public interface IOrderStatus {
    /** @return идентификатор бронируемого фотозала */
    UUID getRoomId();
    /** @return детальная информация о заказе (время, оборудование, фотограф) */
    IOrder getBody();

    void setRoomId(UUID roomId);
    void setBody(IOrder body);
}
