package pi.focus.server.api.context;

import java.util.List;

import pi.focus.server.api.models.IAboutDataBlock;
import pi.focus.server.api.models.IDataCard;
import pi.focus.server.api.models.ITab;
import pi.focus.server.api.models.ITextCard;

public interface IInfoContext {
    IAboutDataBlock getAboutBlock();
    List<ITextCard> getRentRules();
    List<ITab<IDataCard>> getDataTabs();
}
