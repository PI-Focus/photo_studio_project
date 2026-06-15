package pi.focus.server.core.domain;

import java.util.UUID;

/**
 * Модель фотозала.
 * @param id уникальный идентификатор зала
 * @param title название (например, "Лофт")
 * @param description описание интерьера и условий
 * @param price базовая стоимость аренды в час
 */
public record Room(
        UUID id,
        String title,
        String description,
        Integer price
) { }

