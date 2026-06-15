package pi.focus.server.core.service.api;

import pi.focus.server.api.models.ICalendar;
import pi.focus.server.core.domain.User;
import pi.focus.server.service.models.ReservationDto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface IUserService {
    boolean createUser(User user);
    ICalendar getUserCalendar(UUID id, LocalDate day);
    List<ReservationDto> getUserReservationDtos(UUID id, LocalDate day);
    String updateUser(User user);
}
