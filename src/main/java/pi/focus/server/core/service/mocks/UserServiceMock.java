package pi.focus.server.core.service.mocks;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pi.focus.server.api.models.ICalendar;
import pi.focus.server.core.domain.User;
import pi.focus.server.core.service.api.IUserService;
import pi.focus.server.service.models.ReservationDto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Мок-реализация сервиса пользователей.
 * В текущей версии всегда отклоняет регистрацию (возвращает false).
 */
@Service
@Profile({"mock", "test"})
public class UserServiceMock implements IUserService {
    /**
     * Имитирует попытку регистрации.
     * @return всегда false для проверки обработки ошибок регистрации
     */
    @Override
    public boolean createUser(User user) {
        return false;
    }

    @Override
    public ICalendar getUserCalendar(UUID id, LocalDate day) {
        return null;
    }

    @Override
    public List<ReservationDto> getUserReservationDtos(UUID id, LocalDate day) {
        return List.of();
    }

    @Override
    public String updateUser(User user) {
        return null;
    }
}
