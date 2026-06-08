package pi.focus.server.service.context;

import pi.focus.server.api.context.IInfoContext;
import pi.focus.server.api.models.IAboutDataBlock;
import pi.focus.server.api.models.IDataTab;
import pi.focus.server.api.models.ITextCard;

import java.util.List;

public record InfoContext(
        IAboutDataBlock aboutDataBlock,
        List<ITextCard> rentRules,
        List<IDataTab> imagedTabs
) implements IInfoContext {
    @Override
    public IAboutDataBlock getAboutBlock() {
        return aboutDataBlock;
    }

    @Override
    public List<ITextCard> getRentRules() {
        return rentRules;
    }

    @Override
    public List<IDataTab> getImagedTabs() {
        return imagedTabs;
    }
}
