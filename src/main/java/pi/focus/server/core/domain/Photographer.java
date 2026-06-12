package pi.focus.server.core.domain;

import java.util.UUID;

public record Photographer(
        UUID id,
        String name,
        String surname,
        String description,
        String photoPath
) { }




