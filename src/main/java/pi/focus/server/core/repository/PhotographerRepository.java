package pi.focus.server.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pi.focus.server.core.entity.PhotographerEntity;

import java.util.UUID;

/**
 * Репозиторий для работы с сущностями фотографов.
 */
public interface PhotographerRepository extends JpaRepository<PhotographerEntity, UUID> {
}
