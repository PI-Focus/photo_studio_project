package pi.focus.server.api.context;

import java.util.List;

/**
 * Пример контекста для демонстрационных и текстовых страниц
 * Используется для корректности отображения различных типов данных
 */
public interface IExampleContext {
    int getNumber();
    List<Integer> getNumbers();
    String getTextString();
    List<String> getTextStrings();
    String getPicture();
    List<String> getPictures();
}
