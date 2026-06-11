package pi.focus.server.service.context;

import pi.focus.server.api.context.IInfoContext;
import pi.focus.server.api.models.IAboutDataBlock;
import pi.focus.server.api.models.IDataCard;
import pi.focus.server.api.models.ITab;
import pi.focus.server.api.models.ITextCard;

import java.util.List;

public record InfoContextDto(
    IAboutDataBlock aboutBlock,
    List<ITextCard> rentRules,
    List<ITab<IDataCard>> dataTabs
) implements IInfoContext {

    @Override
    public IAboutDataBlock getAboutBlock() {
        return aboutBlock;
    }

    @Override
    public List<ITextCard> getRentRules() {
        return rentRules;
    }

    @Override
    public List<ITab<IDataCard>> getDataTabs() {
        return dataTabs;
    }
}
