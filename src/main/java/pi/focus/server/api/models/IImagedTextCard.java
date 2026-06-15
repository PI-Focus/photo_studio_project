package pi.focus.server.api.models;

/** 
 * Текстовая карточка с поддержкой изображения
 * Расширяет ITextCard, добавляя визуальный контент. 
 */
public interface IImagedTextCard extends ITextCard {
    /** @return путь к файлу изображения или URL-ссылка */
    String getImage();
}
