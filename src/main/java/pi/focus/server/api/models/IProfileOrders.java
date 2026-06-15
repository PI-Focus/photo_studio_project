package pi.focus.server.api.models;

import java.util.List;

/**
 * Интерфейс модели данных для страницы личного кабинета.
 * Объединяет данные пользователя, календарь и список его бронирований.
 */
public interface IProfileOrders {
    /** @return логин владельца профиля */
    String getLogin();
    /** @return календарь занятости, связанный с заказами пользователя */
    ICalendar getCalendar();
    /** @return список подтвержденных бронирований пользователя */
    List<IReservation> getReservations();
}
