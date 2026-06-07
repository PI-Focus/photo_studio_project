package pi.focus.server.service.models.mocks;

import pi.focus.server.api.models.ITextCard;

public class TextCardMock implements ITextCard{
    private final String title;
    private final String text;

    public TextCardMock(String title, String text) {
        this.title = title;
        this.text = text;
    }
    
    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getText() {
        return text;
    }
    
}
