package pi.focus.server.service.models;

import pi.focus.server.api.models.ITextCard;

public record TextCard(String title, String text) implements ITextCard {
    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getText() {
        return text;
    }
}