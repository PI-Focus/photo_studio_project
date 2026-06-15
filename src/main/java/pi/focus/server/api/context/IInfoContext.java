package pi.focus.server.api.context;

import java.util.List;

import pi.focus.server.api.models.IAboutDataBlock;
import pi.focus.server.api.models.IDataCard;
import pi.focus.server.api.models.INamedData;
import pi.focus.server.api.models.ITextCard;

/**
 * Контекст информациионной страницы.
 * Содержит блок «О нас», правила аренды и вкладки с обзором услуг.
 */
public interface IInfoContext {
    /** @return данные для блока «О компании»  */
    IAboutDataBlock getAboutBlock();
    /**@return список правил аренды студии */
    List<ITextCard> getRentRules();
    /** @return данные для навигационных вкладок */
    List<INamedData<IDataCard>> getDataTabs();
}
