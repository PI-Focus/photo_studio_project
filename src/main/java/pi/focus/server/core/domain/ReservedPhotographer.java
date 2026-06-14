package pi.focus.server.core.domain;

import java.util.UUID;

public record ReservedPhotographer (
        UUID reservedPhotographerId,
        UUID reservation_id,
        UUID photographer_id
) { }
