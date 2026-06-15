package pi.focus.server.core.domain;

import java.time.LocalDate;
import java.util.UUID;

public record Reservation(
        UUID id ,
        UUID userId,
        UUID roomId,
        LocalDate day,
        Short fromTime,
        Short toTime
) { }
