package pi.focus.server.core.domain;

import java.util.UUID;

/**
 * Связь выбранного фотографа с конкретным заказом.
 */
public record ReservedPhotographer (
        UUID reservedPhotographerId,
        UUID reservation_id,
        UUID photographer_id
) { }
