package pi.focus.server.core.service.mocks;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pi.focus.server.api.models.IOrderStatus;
import pi.focus.server.core.service.api.IOrderFacade;

import java.util.UUID;

@Service
@Profile({"mock", "test"})
public class OrderFacadeMock implements IOrderFacade {
    @Override
    public IOrderStatus getEmptyOrderStatus() {
        return null;
    }

    @Override
    public Integer validateOrderStatus(IOrderStatus orderStatus) {
        return -1;
    }

    @Override
    public void createReservation(UUID id, IOrderStatus orderStatus) {
    }
}
