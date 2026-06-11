package pi.focus.server.api.context;

import java.util.List;

import pi.focus.server.api.models.IAboutDataBlock;
import pi.focus.server.api.models.IDataCard;
import pi.focus.server.api.models.INamedData;
import pi.focus.server.api.models.ITextCard;

public interface IInfoContext {
    IAboutDataBlock getAboutBlock();
    List<ITextCard> getRentRules();
    List<INamedData<IDataCard>> getDataTabs();
}
