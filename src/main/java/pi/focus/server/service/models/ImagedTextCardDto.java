package pi.focus.server.service.models;

import pi.focus.server.api.models.IImagedTextCard;

/**
 * DTO для карточки, содержащей текст и изображение.
 * 
 * @param title заголовок
 * @param text описание
 * @param imageUrl путь к картинке
 */
public record ImagedTextCardDto(String title, String text, String imageUrl) implements IImagedTextCard {
    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public String getImage() {
        return imageUrl;
    }
}
