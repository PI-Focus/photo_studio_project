package pi.focus.server.core.service.mocks;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pi.focus.server.core.service.api.IReservationService;

import java.util.UUID;

@Service
@Profile({"mock", "test"})
public class ReservationServiceMock implements IReservationService {
    @Override
    public Boolean deleteOrderById(UUID id) {
        return false;
    }
}
