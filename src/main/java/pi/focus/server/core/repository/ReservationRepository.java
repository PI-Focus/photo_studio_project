package pi.focus.server.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pi.focus.server.core.entity.ReservationEntity;

import java.util.UUID;

public interface ReservationRepository extends JpaRepository<ReservationEntity, UUID> {
}
