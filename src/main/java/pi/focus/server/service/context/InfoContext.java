package pi.focus.server.service.context;

import pi.focus.server.api.context.IInfoContext;
import pi.focus.server.api.models.IAboutDataBlock;
import pi.focus.server.api.models.IDataCard;
import pi.focus.server.api.models.ITab;
import pi.focus.server.api.models.ITextCard;

import java.util.List;

public record InfoContext(
        IAboutDataBlock aboutDataBlock,
        List<ITextCard> rentRules,
        List<ITab<IDataCard>> imagedTabs
) implements IInfoContext {
    // TODO: delete this

    @Override
    public IAboutDataBlock getAboutBlock() {
        return aboutDataBlock;
    }

    @Override
    public List<ITextCard> getRentRules() {
        return rentRules;
    }

    @Override
    public List<ITab<IDataCard>> getDataTabs() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDataTabs'");
    }


}
