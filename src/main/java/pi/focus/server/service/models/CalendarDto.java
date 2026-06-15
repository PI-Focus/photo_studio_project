package pi.focus.server.service.models;

import pi.focus.server.api.models.ICalendar;

import java.util.List;

/**
 * DTO для передачи сетки календаря.
 * Представляет собой матрицу целых чисел, где каждое число кодирует статус ячейки.
 */
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
