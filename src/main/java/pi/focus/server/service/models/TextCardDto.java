package pi.focus.server.service.models;

import pi.focus.server.api.models.ITextCard;

/**
 * Простейший DTO для текстовой карточки.
 * 
 * @param title заголовок блока
 * @param text содержимое текста
 */
public record TextCardDto(String title, String text) implements ITextCard{
    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getText() {
        return text;
    }
}
