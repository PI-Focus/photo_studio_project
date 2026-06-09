package pi.focus.server.service.context.mocks;

import java.util.List;

import pi.focus.server.api.context.IEquipmentContext;
import pi.focus.server.api.models.IImagedTextCard;
import pi.focus.server.service.models.mocks.ImagedTextCardMock;

public class EquipmentContextMock implements IEquipmentContext{
    private final List<IImagedTextCard> equipment;

    public EquipmentContextMock() {
        equipment = List.of(
            new ImagedTextCardMock("Оборудованя 1", "Штучка для фоторграфирования 1", MocksDefines.TEST_IMAGE_PATH),
            new ImagedTextCardMock("Оборудованя 2", "Штучка для фоторграфированя 2", MocksDefines.TEST_IMAGE_PATH),
            new ImagedTextCardMock("Оборудованя 3", "Штучка для нефотогрофированя 3", MocksDefines.TEST_IMAGE_PATH)
        );
    }

    @Override
    public List<IImagedTextCard> getEquipment() {
        return equipment;
    }
    
}
