package pi.focus.server.service.models.mocks;

import pi.focus.server.api.models.IImagedTab;

public class ImagedTabMock extends ImagedTextCardMock implements IImagedTab{
    private String tabName;
    private String linkUrl;

    public ImagedTabMock(String title, String text, String imageUrl, String tabName, String linkUrl) {
        super(title, text, imageUrl);
        this.tabName = tabName;
        this.linkUrl = linkUrl;
    }

    @Override
    public String getTabName() {
        return tabName;
    }

    @Override
    public String getLinkUrl() {
        return linkUrl;
    }
}
