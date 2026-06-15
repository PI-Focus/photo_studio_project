package pi.focus.server.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pi.focus.server.core.entity.RoomEntity;

import java.util.UUID;

/**
 * Репозиторий для управления фотозалами.
 */
public interface RoomRepository extends JpaRepository<RoomEntity, UUID> {
}
