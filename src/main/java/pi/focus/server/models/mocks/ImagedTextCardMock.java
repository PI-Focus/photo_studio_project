package pi.focus.server.models.mocks;

import pi.focus.server.models.IImagedTextCard;

public class ImagedTextCardMock extends TextCardMock implements IImagedTextCard {
    private String imageUrl;

    public ImagedTextCardMock(String title, String text, String imageUrl) {
        super(title, text);
        this.imageUrl = imageUrl;
    }

    @Override
    public String getImageUrl() {
        return imageUrl;
    }
}
