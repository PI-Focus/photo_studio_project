package pi.focus.server.service.models;


import pi.focus.server.api.models.ICalendar;
import pi.focus.server.api.models.IProfileOrders;
import pi.focus.server.api.models.IReservation;
import tools.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

/**
 * Агрегирующий DTO для отображения информации в профиле пользователя.
 * Содержит имя пользователя, календарь занятости и список бронирований.
 */
public record ProfileOrdersDto(
        String login,
        @JsonDeserialize(as = CalendarDto.class)
        ICalendar calendar,
        @JsonDeserialize(as = ArrayList.class, contentAs = ReservationDto.class)
        List<IReservation> reservations
) implements IProfileOrders {
    @Override
    public String getLogin() {
        return login;
    }

    @Override
    public ICalendar getCalendar() {
        return calendar;
    }

    @Override
    public List<IReservation> getReservations() {
        return reservations;
    }
}
