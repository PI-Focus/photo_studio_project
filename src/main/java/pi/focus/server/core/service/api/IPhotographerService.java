package pi.focus.server.core.service.api;

import io.hypersistence.utils.hibernate.type.range.Range;
import pi.focus.server.api.context.IPhotographersContext;
import pi.focus.server.core.domain.Photographer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface IPhotographerService {
    IPhotographersContext getEquipmentContext();
    List<Photographer> getPhotographersByTime(Range<LocalDateTime> time);
    Boolean exists(UUID id);
    Photographer getPhotographerById(UUID id);
    boolean freePhotographer(UUID id, Range<LocalDateTime> time);
}
