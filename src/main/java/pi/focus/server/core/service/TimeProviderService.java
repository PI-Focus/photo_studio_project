package pi.focus.server.core.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Сервис для предоставления текущего времени с учетом настроек часового пояса приложения.
 * Используется для обеспечения единого источника времени во всей системе.
 */
@Component
public class TimeProviderService {
    /** Идентификатор часового пояса, используемый в приложении */
    private final ZoneId zoneId;

    /**
     * Конструктор сервиса.
     * @param timezone строковый идентификатор временной зоны, 
     *                 подгружаемый из конфигурационного файла
     */
    public TimeProviderService(@Value("${app.timezone}") String timezone) {
        this.zoneId = ZoneId.of(timezone);
    }

    /**
     * Возвращает текущую дату и время в установленном часовом поясе.
     * @return объект ZonedDateTime, представляющий текущий момент времени
     */
    public ZonedDateTime now() {
        return ZonedDateTime.now(zoneId);
    }
}