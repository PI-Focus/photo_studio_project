package pi.focus.server.service.models.mocks;

import pi.focus.server.api.models.IDataTab;

public class DataTabMock extends DataCardMock implements IDataTab{
    private final String tabName;

    public DataTabMock(String title, String text, String imageUrl, String linkUrl, String tabName) {
        super(title, text, imageUrl, linkUrl);
        this.tabName = tabName;
    }

    @Override
    public String getTabName() {
        return tabName;
    }
}
