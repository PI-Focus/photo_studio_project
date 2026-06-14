package pi.focus.server.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pi.focus.server.core.entity.EquipmentEntity;

import java.util.UUID;

@Repository
public interface EquipmentRepository extends JpaRepository<EquipmentEntity, UUID> {
}
