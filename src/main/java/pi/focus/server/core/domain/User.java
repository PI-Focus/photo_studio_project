package pi.focus.server.core.domain;

import java.util.UUID;

public record User(
        UUID id,
        String login,
        String password,
        UserRole role
) { }