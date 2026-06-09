package pi.focus.server.service.models.mocks;

import pi.focus.server.api.models.IDataCard;

public class DataCardMock extends ImagedTextCardMock implements IDataCard {
    private final String linkUrl;

    public DataCardMock(String title, String text, String imageUrl, String linkUrl) {
        super(title, text, imageUrl);
        this.linkUrl = linkUrl;
    }

    @Override
    public String getLinkUrl() {
        return linkUrl;
    }
}
