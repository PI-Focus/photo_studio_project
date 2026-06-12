package pi.focus.server.core.domain;

import java.util.UUID;

public record Photo(
        UUID id,
        UUID roomId,
        String path
) { }

