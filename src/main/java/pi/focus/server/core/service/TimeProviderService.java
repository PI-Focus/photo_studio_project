package pi.focus.server.core.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class TimeProviderService {
    private final ZoneId zoneId;

    public TimeProviderService(@Value("${app.timezone}") String timezone) {
        this.zoneId = ZoneId.of(timezone);
    }

    public ZonedDateTime now() {
        return ZonedDateTime.now(zoneId);
    }
}