package pi.focus.server.service.models;

import pi.focus.server.core.service.api.IOrderId;

import java.util.UUID;

/**
 * Простой контейнер для передачи идентификатора заказа.
 */
public record OrderIdDto(UUID orderId) implements IOrderId {
    @Override
    public UUID getOrderId() {
        return orderId;
    }
}
