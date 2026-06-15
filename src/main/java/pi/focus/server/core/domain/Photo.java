package pi.focus.server.core.domain;

import java.util.UUID;

/**
 * Фотография интерьера зала.
 * @param id уникальный идентификатор фотографии
 * @param roomId идентификатор зала, к которому относится фото
 * @param path путь к файлу изображения
 */
public record Photo(
        UUID id,
        UUID roomId,
        String path
) { }

