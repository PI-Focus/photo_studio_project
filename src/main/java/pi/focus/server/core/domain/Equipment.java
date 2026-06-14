package pi.focus.server.core.domain;

import java.util.UUID;

public record Equipment(
        UUID id,
        String title,
        String description,
        Integer price,
        String photoPath
) { }

