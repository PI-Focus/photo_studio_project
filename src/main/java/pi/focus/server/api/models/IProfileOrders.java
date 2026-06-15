package pi.focus.server.api.models;

import java.util.List;

public interface IProfileOrders {
    String getLogin();
    ICalendar getCalendar();
    List<IReservation> getReservations();
}
