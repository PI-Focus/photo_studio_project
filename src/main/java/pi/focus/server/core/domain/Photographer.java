package pi.focus.server.core.domain;

import java.util.UUID;

/**
 * Модель фотографа.
 * @param id уникальный идентификатор
 * @param name имя
 * @param surname фамилия
 * @param description информация о профессиональном опыте
 * @param price почасовая ставка
 * @param photoPath путь к портретному фото
 */
public record Photographer(
        UUID id,
        String name,
        String surname,
        String description,
        Integer price,
        String photoPath
) { }




