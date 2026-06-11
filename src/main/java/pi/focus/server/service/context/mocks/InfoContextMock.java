package pi.focus.server.service.context.mocks;

import java.util.List;

import pi.focus.server.api.context.IInfoContext;
import pi.focus.server.api.models.IAboutDataBlock;
import pi.focus.server.api.models.IDataCard;
import pi.focus.server.api.models.INamedData;
import pi.focus.server.api.models.ITextCard;
import pi.focus.server.service.models.AboutDataBlockDto;
import pi.focus.server.service.models.DataCardDto;
import pi.focus.server.service.models.TabDto;
import pi.focus.server.service.models.TextCardDto;

public class InfoContextMock implements IInfoContext {
    private final List<ITextCard> rentRules;
    private final List<INamedData<IDataCard>> imagedTabs;
    private final AboutDataBlockDto aboutData;

    public InfoContextMock() {
        rentRules = List.of(
            new TextCardDto("Первое правило", "Не упоминать о бойцовском клубе."),
            new TextCardDto("Второе правило", "Нигде не упоминать о бойцовском клубе."),
            new TextCardDto("Третье правило", "Если противник крикнул «стоп», выдохся или отключился — бой окончен."),
            new TextCardDto("Четвёртое правило", "В бою участвуют лишь двое."),
            new TextCardDto("Пятое правило", "В один вечер — только один поединок."),
            new TextCardDto("Шестое правило", "Снимать обувь и рубашку."),
            new TextCardDto("Седьмое правило", "Бой продолжается столько, сколько потребуется."),
            new TextCardDto("Восьмое правило", "Тот, кто впервые пришел в клуб, примет бой.")
        );

        imagedTabs = List.of(
            new TabDto<>("ЗАЛЫ", new DataCardDto("Залы", "У нас вы можете выбрать зал йоу", MocksDefines.TEST_IMAGE_PATH, "/photorooms")),
            new TabDto<>("ОБОРУДОВАНИЕ", new DataCardDto("Оборудование", "У нас вы можете выбрать оборудование йоу", MocksDefines.TEST_IMAGE_PATH, "/equipment")),
            new TabDto<>("ФОТОГРАФЫ", new DataCardDto("Фотографы", "У нас вы можете выбрать фотографов йоу", MocksDefines.TEST_IMAGE_PATH, "/photographers"))
        );

        aboutData = new AboutDataBlockDto(
            "/images/logo-bright.svg",
            "Фотостудия «Фокус» в Ярославле — это 5 отдельных пространств, чтобы каждая съёмка была в своей вселенной. " +
            "Минимализм, характерные интерьеры, бесшумный хромакей и профильный свет. Вам не придётся тащить с собой полкомнаты. " +
            "Тумбы, стойки, отражатели, объективы — всё под рукой. Если своего мастера кадра нет, за пульт встают наши фотографы. " +
            "Никакого «делаем вид, что всё хорошо». Только живая съёмка, чистый свет и фокус на деталях. ",
            MocksDefines.TEST_IMAGE_PATH
        );
    }

    @Override
    public IAboutDataBlock getAboutBlock() {
        return aboutData;
    }

    @Override
    public List<ITextCard> getRentRules() {
        return rentRules;
    }

    @Override
    public List<INamedData<IDataCard>> getDataTabs() {
        return imagedTabs;
    }
    
}
