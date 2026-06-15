package pi.focus.server.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pi.focus.server.core.entity.EquipmentEntity;

import java.util.UUID;

/**
 * Репозиторий для работы с сущностями оборудования.
 */
public interface EquipmentRepository extends JpaRepository<EquipmentEntity, UUID> {
}
