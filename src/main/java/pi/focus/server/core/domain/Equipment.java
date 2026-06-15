package pi.focus.server.core.domain;

import java.util.UUID;

/**
 * Модель оборудования для аренды.
 * @param id уникальный идентификатор
 * @param title название оборудования
 * @param description описание характеристик
 * @param price стоимость аренды
 * @param photoPath путь к фотографии
 */
public record Equipment(
        UUID id,
        String title,
        String description,
        Integer price,
        String photoPath
) { }

