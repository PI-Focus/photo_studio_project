package pi.focus.server.core.domain;

import java.util.UUID;

public record Room(
        UUID id,
        String title,
        String description
) { }

