package pi.focus.server.service.models;

import pi.focus.server.api.models.IDataCard;

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
