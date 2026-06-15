package pi.focus.server.core.mapper;

import pi.focus.server.core.domain.Photographer;
import pi.focus.server.core.entity.PhotographerEntity;

import java.util.ArrayList;

/**
 * Маппер для преобразования данных о фотографах.
 * Выполняет перевод данных между слоем сущностей  и слоем домена.
 */
public final class PhotographerMapper {
    /** Приватный конструктор: маппер является утилитарным классом */
    private PhotographerMapper() {
    }

    /**
     * Конвертирует сущность БД в доменную запись фотографа.
     * 
     * @param photographerEntity сущность из базы данных
     * @return объект Photographer с данными из сущности
     */
    public static Photographer toDomain(PhotographerEntity photographerEntity) {
        return new Photographer(
                photographerEntity.getId(),
                photographerEntity.getName(),
                photographerEntity.getSurname(),
                photographerEntity.getDescription(),
                photographerEntity.getPrice(),
                photographerEntity.getPhotoPath()
        );
    }

    /**
     * Конвертирует данные фотографа из домена в сущность БД.
     * 
     * @param photographer доменный объект фотографа
     * @return новая сущность PhotographerEntity
     */
    public static PhotographerEntity toEntity(Photographer photographer) {
        return new PhotographerEntity(
                photographer.id(),
                photographer.name(),
                photographer.surname(),
                photographer.description(),
                photographer.price(),
                photographer.photoPath(),
                new ArrayList<>()
        );
    }
}
