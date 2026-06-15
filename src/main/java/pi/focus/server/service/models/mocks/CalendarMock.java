package pi.focus.server.service.models.mocks;

import pi.focus.server.api.models.ICalendar;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.time.temporal.ChronoUnit;

/**
 * Mock-реализация календаря для тестирования фронтенда.
 * Генерирует циклическую сетку занятости на основе базового шаблона,
 * которая сдвигается в зависимости от выбранной даты.
 */
public class CalendarMock implements ICalendar {
    private static final Integer ROWS = 7;
    private static final Integer COLUMNS = 14;
    
    /** Базовый шаблон занятости (1000 — занято, -1 — свободно) */
    private static final List<List<Integer>> BASE_CALENDAR = Arrays.asList(
        Arrays.asList(1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000), 
        Arrays.asList(1000, 1000, 1000, 1000, -1,   -1,   -1,   -1,   1000, 1000, 1000, 1000, 1000, 1000), 
        Arrays.asList(-1,   -1,   -1,   -1,   1000, 1000, 1000, 1000, -1,   -1,   -1,   -1,   -1,   -1),   
        Arrays.asList(-1,   1000, 1000, -1,   -1,   1000, -1,   -1,   -1,   1000, 1000, 1000, -1,   -1),   
        Arrays.asList(1000, -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1),   
        Arrays.asList(-1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   1000), 
        Arrays.asList(-1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1)    
    );

    private final List<List<Integer>> calendar;

    /**
     * Конструктор мока. Если дата в прошлом — календарь пуст.
     * Если в будущем — строки шаблона циклически перемешиваются.
     * @param targetDate целевая дата календаря
     */
    public CalendarMock(LocalDate targetDate) {
        LocalDate today = LocalDate.now();

        if (targetDate.isBefore(today)) {
            this.calendar = null;
            return;
        }

        long daysBetween = ChronoUnit.DAYS.between(today, targetDate);

        this.calendar = new ArrayList<>();

        for (int i = 0; i < ROWS; i++) {
            int baseIndex = (int) ((i + daysBetween / ROWS) % ROWS);
            
            this.calendar.add(new ArrayList<>(BASE_CALENDAR.get(baseIndex)));
        }
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

