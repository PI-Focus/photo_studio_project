package pi.focus.server.core.service.api;

import pi.focus.server.api.models.IOrderStatus;

import java.util.UUID;


public interface IOrderFacade {
    IOrderStatus getEmptyOrderStatus();
    Integer validateOrderStatus(IOrderStatus orderStatus);
    void createReservation(UUID id, IOrderStatus orderStatus);
}
