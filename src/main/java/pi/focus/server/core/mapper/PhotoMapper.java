package pi.focus.server.core.mapper;

import pi.focus.server.core.domain.Photo;
import pi.focus.server.core.entity.PhotoEntity;

/**
 * Маппер для работы с объектами фотографий.
 * Отвечает за преобразование между и доменной моделью.
 */
public final class PhotoMapper {
    /** Приватный конструктор: создание экземпляров запрещено */
    private PhotoMapper() {
    }

    /**
     * Преобразует сущность фотографии в доменную модель.
     * Извлекает ID комнаты из связанной сущности зала.
     * 
     * @param photoEntity сущность фотографии
     * @return объект Photo с идентификатором комнаты
     */
    public static Photo toDomain(PhotoEntity photoEntity) {
        return new Photo(
                photoEntity.getId(),
                photoEntity.getRoom().getId(),
                photoEntity.getPath()
        );
    }

    /**
     * Преобразует доменную модель фотографии в сущность БД.
     * 
     * @param photo объект доменной модели
     * @return сущность PhotoEntity
     */
    public static PhotoEntity toEntity(Photo photo) {
        return new PhotoEntity(
                photo.id(),
                null,
                photo.path()
        );
    }
}
