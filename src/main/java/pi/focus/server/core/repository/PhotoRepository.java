package pi.focus.server.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pi.focus.server.core.entity.PhotoEntity;

import java.util.UUID;

public interface PhotoRepository extends JpaRepository<PhotoEntity, UUID> {
}
