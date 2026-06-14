package pi.focus.server.core.mapper;

import pi.focus.server.core.domain.Photo;
import pi.focus.server.core.entity.PhotoEntity;


public final class PhotoMapper {
    private PhotoMapper() {
    }

    public static Photo toDomain(PhotoEntity photoEntity) {
        return new Photo(
                photoEntity.getId(),
                photoEntity.getRoom().getId(),
                photoEntity.getPath()
        );
    }

    public static PhotoEntity toEntity(Photo photo) {
        return new PhotoEntity(
                photo.id(),
                null,
                photo.path()
        );
    }
}
