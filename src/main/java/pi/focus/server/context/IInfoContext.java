package pi.focus.server.context;

import java.util.List;

import pi.focus.server.models.IAboutDataBlock;
import pi.focus.server.models.IImagedTab;
import pi.focus.server.models.ITextCard;

public interface IInfoContext {
    IAboutDataBlock getAboutBlock();
    List<ITextCard> getRentRules();
    List<IImagedTab> getImagedTabs();
}
