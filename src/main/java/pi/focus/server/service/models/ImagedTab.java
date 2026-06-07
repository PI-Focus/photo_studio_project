package pi.focus.server.service.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import pi.focus.server.api.models.IImagedTab;
import pi.focus.server.service.models.mocks.ImagedTextCardMock;

public class ImagedTab extends ImagedTextCardMock implements IImagedTab {
    private final String tabName;
    private final String linkUrl;

    @JsonCreator
    public ImagedTab(
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