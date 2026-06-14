package pi.focus.server.service.models;

import pi.focus.server.api.models.ICalendar;

import java.util.List;

public record CalendarDto(List<List<Integer>> calendar) implements ICalendar {
    private static final Integer ROWS = 14;
    private static final Integer COLUMNS = 7;

    @Override
    public Integer getROWS() {
        return ROWS;
    }

    @Override
    public Integer getCOLUMNS() {
        return COLUMNS;
    }

    @Override
    public List<List<Integer>> getCalendar() {
        return calendar;
    }
}
