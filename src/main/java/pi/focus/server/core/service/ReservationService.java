package pi.focus.server.core.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pi.focus.server.core.service.api.IReservationService;

@Service
@Profile({"dev", "prod", "test"})
public class ReservationService implements IReservationService {
}
