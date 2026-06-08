package pi.focus.server.service.context.mocks;

import java.util.List;

import pi.focus.server.api.context.IInfoContext;
import pi.focus.server.api.models.IAboutDataBlock;
import pi.focus.server.api.models.IDataTab;
import pi.focus.server.api.models.ITextCard;
import pi.focus.server.service.models.mocks.AboutDataBlockMock;
import pi.focus.server.service.models.mocks.DataTabMock;
import pi.focus.server.service.models.mocks.TextCardMock;

public class InfoContextMock implements IInfoContext {
    private final List<ITextCard> rentRules;
    private final List<IDataTab> imagedTabs;

    public InfoContextMock() {
        rentRules = List.of(
            new TextCardMock("Первое правило", "Не упоминать о бойцовском клубе."),
            new TextCardMock("Второе правило", "Нигде не упоминать о бойцовском клубе."),
            new TextCardMock("Третье правило", "Если противник крикнул «стоп», выдохся или отключился — бой окончен."),
            new TextCardMock("Четвёртое правило", "В бою участвуют лишь двое."),
            new TextCardMock("Пятое правило", "В один вечер — только один поединок."),
            new TextCardMock("Шестое правило", "Снимать обувь и рубашку."),
            new TextCardMock("Седьмое правило", "Бой продолжается столько, сколько потребуется."),
            new TextCardMock("Восьмое правило", "Тот, кто впервые пришел в клуб, примет бой.")
        );

        imagedTabs = List.of(
            new DataTabMock("Залы", "У нас вы можете выбрать зал йоу", null, "ЗАЛЫ", "/photorooms"),
            new DataTabMock("Оборудование", "У нас вы можете выбрать оборудование йоу", null, "ОБОРУДОВАНИЕ", "/equipment"),
            new DataTabMock("Фотографы", "У нас вы можете выбрать фотографов йоу", null, "ФОТОГРАФЫ", "/photographers")
        );
    }

    @Override
    public IAboutDataBlock getAboutBlock() {
        return new AboutDataBlockMock();
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
