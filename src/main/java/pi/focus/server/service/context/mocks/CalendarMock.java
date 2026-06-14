package pi.focus.server.service.context.mocks;

import pi.focus.server.api.models.ICalendar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CalendarMock implements ICalendar {
    private static final Integer ROWS = 14;
    private static final Integer COLUMNS = 7;
    private final List<List<Integer>> calendar;

    public CalendarMock() {
        calendar = new ArrayList<>();
        calendar.add(new ArrayList<>(Arrays.asList(1000, 1000, -1, -1, 1000, -1, -1)));
        calendar.add(new ArrayList<>(Arrays.asList(1000, 1000, -1, 1000, -1, -1, -1)));
        calendar.add(new ArrayList<>(Arrays.asList(1000, 1000, -1, 1000, -1, -1, -1)));
        calendar.add(new ArrayList<>(Arrays.asList(1000, 1000, -1, -1, -1, -1, -1)));
        calendar.add(new ArrayList<>(Arrays.asList(1000, -1, 1000, -1, -1, -1, -1)));
        calendar.add(new ArrayList<>(Arrays.asList(1000, -1, 1000, 1000, -1, -1, -1)));
        calendar.add(new ArrayList<>(Arrays.asList(1000, -1, 1000, -1, -1, -1, -1)));
        calendar.add(new ArrayList<>(Arrays.asList(1000, -1, 1000, -1, -1, -1, -1)));
        calendar.add(new ArrayList<>(Arrays.asList(1000, 1000, -1, -1, -1, -1, -1)));
        calendar.add(new ArrayList<>(Arrays.asList(1000, 1000, -1, 1000, -1, -1, -1)));
        calendar.add(new ArrayList<>(Arrays.asList(1000, 1000, -1, 1000, -1, -1, -1)));
        calendar.add(new ArrayList<>(Arrays.asList(1000, 1000, -1, 1000, -1, -1, -1)));
        calendar.add(new ArrayList<>(Arrays.asList(1000, 1000, -1, -1, -1, -1, -1)));
        calendar.add(new ArrayList<>(Arrays.asList(1000, 1000, -1, -1, -1, 1000, -1)));
    }

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

