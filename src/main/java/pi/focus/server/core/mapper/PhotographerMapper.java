package pi.focus.server.core.mapper;

import pi.focus.server.core.domain.Photographer;
import pi.focus.server.core.entity.PhotographerEntity;


public final class PhotographerMapper {
    private PhotographerMapper() {
    }

    public static Photographer toDomain(PhotographerEntity photographerEntity) {
        return new Photographer(
                photographerEntity.getId(),
                photographerEntity.getName(),
                photographerEntity.getSurname(),
                photographerEntity.getDescription(),
                photographerEntity.getPhotoPath()
        );
    }

    public static PhotographerEntity toEntity(Photographer photographer) {
        return new PhotographerEntity(
                photographer.id(),
                photographer.name(),
                photographer.surname(),
                photographer.description(),
                photographer.photoPath()
        );
    }
}
