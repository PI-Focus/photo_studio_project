package pi.focus.server.service.models;

import pi.focus.server.core.service.api.IOrderId;

import java.util.UUID;

public record OrderIdDto(UUID orderId) implements IOrderId {
    @Override
    public UUID getOrderId() {
        return orderId;
    }
}
