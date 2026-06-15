package pi.focus.server.api.models;

import java.util.List;

/**
 * Интерфейс для представления сетки календаря занятости.
 * Используется для отображения расписания залов в виде матрицы.
 */
public interface ICalendar {
    /** @return количество строк в сетке календаря (время)*/
    Integer getROWS();
    /** @return количество столбцов в сетке календаря (дни)*/
    Integer getCOLUMNS();
    /** @return двумерный список, представляющий состояние занятости ячеек календаря */
    List<List<Integer>> getCalendar();
}
