package pi.focus.server.service.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import pi.focus.server.api.models.IDataTab;
import pi.focus.server.service.models.mocks.ImagedTextCardMock;

public class DataTab extends ImagedTextCardMock implements IDataTab {
    private final String tabName;
    private final String linkUrl;

    @JsonCreator
    public DataTab(
            @JsonProperty("title") String title,
            @JsonProperty("text") String text,
            @JsonProperty("imageUrl") String imageUrl,
            @JsonProperty("tabName") String tabName,
            @JsonProperty("linkUrl") String linkUrl
    ) {
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