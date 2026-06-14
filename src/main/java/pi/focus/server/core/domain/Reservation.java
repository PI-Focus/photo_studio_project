package pi.focus.server.core.domain;

import io.hypersistence.utils.hibernate.type.range.Range;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record Reservation(
        UUID id ,
        UUID userId,
        UUID roomId,
        Range<LocalDateTime> time
) { }
