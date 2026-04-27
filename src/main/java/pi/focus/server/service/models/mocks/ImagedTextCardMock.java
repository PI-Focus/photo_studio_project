package pi.focus.server.service.models.mocks;

import pi.focus.server.api.models.IImagedTextCard;

public class ImagedTextCardMock extends TextCardMock implements IImagedTextCard {
    private final String imageUrl;

    public ImagedTextCardMock(String title, String text, String imageUrl) {
        super(title, text);
        this.imageUrl = imageUrl;
    }

    @Override
    public String getImageUrl() {
        return imageUrl;
    }
}
