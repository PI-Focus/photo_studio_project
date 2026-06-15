package pi.focus.server.core.service.api;

import pi.focus.server.api.models.IOrderStatus;


public interface IOrderFacade {
    IOrderStatus getEmptyOrderStatus();
    Integer validateOrderStatus(IOrderStatus orderStatus);
}
