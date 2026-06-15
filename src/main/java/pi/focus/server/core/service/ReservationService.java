package pi.focus.server.core.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pi.focus.server.core.repository.ReservationRepository;
import pi.focus.server.core.service.api.IReservationService;

import java.util.UUID;

@Service
@Profile({"dev", "prod", "test"})
public class ReservationService implements IReservationService {
    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public Boolean deleteOrderById(UUID id) {
        if (reservationRepository.existsById(id)) {
            reservationRepository.deleteById(id);
            return true;
        } else {
            return false;
        }

    }
}
