package pi.focus.server.service.models;

import pi.focus.server.api.models.IDataCard;

/**
 * DTO для полнофункциональной карточки данных.
 * Содержит заголовок, описание, изображение и ссылку для перехода.
 * 
 * @param title заголовок карточки
 * @param text основной текст описания
 * @param imageUrl путь к изображению карточки
 * @param linkUrl адрес для перехода при клике на карточку
 */
public record DataCardDto(
    String title, 
    String text, 
    String imageUrl, 
    String linkUrl
) implements IDataCard {

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

    @Override
    public String getLinkUrl() {
        return linkUrl;
    }
}
