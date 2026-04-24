package pi.focus.server.models.mocks;

import pi.focus.server.models.ITextCard;

public class TextCardMock implements ITextCard{
    private String title;
    private String text;

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
