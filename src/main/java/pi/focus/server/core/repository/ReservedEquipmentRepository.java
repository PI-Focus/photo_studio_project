package pi.focus.server.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pi.focus.server.core.entity.ReservedEquipmentEntity;

import java.util.UUID;

public interface ReservedEquipmentRepository extends JpaRepository<ReservedEquipmentEntity, UUID> {
}
