package pi.focus.server.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pi.focus.server.core.entity.ReservedPhotographerEntity;

import java.util.UUID;

public interface ReservedPhotographerRepository extends JpaRepository<ReservedPhotographerEntity, UUID> {
}
