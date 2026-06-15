package pi.focus.server.core.service.mocks;

import io.hypersistence.utils.hibernate.type.range.Range;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pi.focus.server.api.context.IPhotographersContext;
import pi.focus.server.core.domain.Photographer;
import pi.focus.server.core.service.api.IPhotographerService;
import pi.focus.server.service.context.mocks.PhotographersContextMock;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Profile({"mock", "test"})
public class PhotographerServiceMock implements IPhotographerService {
    @Override
    public IPhotographersContext getEquipmentContext() {
        return new PhotographersContextMock();
    }

    @Override
    public List<Photographer> getPhotographersByTime(Range<LocalDateTime> time) {
        return List.of();
    }

    @Override
    public Boolean exists(UUID id) {
        return false;
    }

    @Override
    public Photographer getPhotographerById(UUID id) {
        return null;
    }

    @Override
    public boolean freePhotographer(UUID id, Range<LocalDateTime> time) {
        return false;
    }
}
