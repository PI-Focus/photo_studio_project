package pi.focus.server.api.models;

/** 
 * Базовый интерфейс текстовой карточки.
 * Содержит минимальный набор данных для отображения информационного блока.
 */
public interface ITextCard {
    /** @return заголовок карточки */
    String getTitle();
    /** @return основной текстовый контент карточки */
    String getText();
}
