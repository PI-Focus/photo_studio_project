package pi.focus.server.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pi.focus.server.core.entity.EquipmentEntity;

import java.util.UUID;

public interface EquipmentRepository extends JpaRepository<EquipmentEntity, UUID> {
}
