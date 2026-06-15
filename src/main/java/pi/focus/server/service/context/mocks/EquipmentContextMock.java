package pi.focus.server.service.context.mocks;

import java.util.List;

import pi.focus.server.api.context.IEquipmentContext;
import pi.focus.server.api.models.IImagedTextCard;
import pi.focus.server.service.models.ImagedTextCardDto;

/**
 * Мок-реализация контекста оборудования.
 * Содержит список техники.
 */
public class EquipmentContextMock implements IEquipmentContext{
    private final List<IImagedTextCard> equipment;

    public EquipmentContextMock() {
        equipment = List.of(
            new ImagedTextCardDto("Оборудованя 1", "Штучка для фоторграфирования 1", MocksDefines.TEST_IMAGE_PATH),
            new ImagedTextCardDto("Оборудованя 2", "Штучка для фоторграфированя 2" +
            "\n\n ой а знаете почему бы не добавить сюда какой-нибудб длинющий текст чтобы узнать как оно себя поведет." +
            " Ведь реально интересно надо будет наверное в мок дефайнс запихать парочку супер длинных строк чтобы посмотреть " + 
            "насколько печально с этим справится моя вёрстка. Ну наверное на пока хватит, там уже гляну норм не норм кайф не кайф." +
            "\n\nНо как оказалось текст этот недостатоточно длинный так что Lorem ipsum, dolor sit amet consectetur adipisicing elit." + 
            " Ut neque at, reprehenderit dolor aperiam, debitis delectus qui asperiores error veritatis nisi aliquam saepe quo itaque" + 
            " deleniti excepturi distinctio, numquam veniam! Lorem ipsum dolor sit amet consectetur adipisicing elit. Officia minus incidunt," + 
            " harum porro magni inventore, doloremque libero quam, ullam necessitatibus iusto at omnis quisquam saepe voluptate adipisci" +
            " eaque in vitae? Lorem ipsum dolor sit amet consectetur adipisicing elit. Blanditiis architecto suscipit impedit dolorum nihil," + 
            " est vitae at molestias, dignissimos fugit consectetur! Doloremque rerum dolores itaque assumenda id sunt soluta architecto!", 
            MocksDefines.TEST_IMAGE_PATH),
            new ImagedTextCardDto("Оборудованя 3", "Штучка для нефотогрофированя 3", MocksDefines.TEST_IMAGE_PATH)
        );
    }

    @Override
    public List<IImagedTextCard> getEquipment() {
        return equipment;
    }
    
}
