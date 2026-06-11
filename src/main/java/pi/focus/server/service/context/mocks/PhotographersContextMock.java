package pi.focus.server.service.context.mocks;

import java.util.List;

import pi.focus.server.api.context.IPhotographersContext;
import pi.focus.server.api.models.IImagedTextCard;
import pi.focus.server.service.models.ImagedTextCardDto;

public class PhotographersContextMock implements IPhotographersContext{
    private final List<IImagedTextCard> equipment;

    public PhotographersContextMock() {
        equipment = List.of(
            new ImagedTextCardDto("Иванов Акакий Иваныч", "Фоткает жостко", MocksDefines.TEST_IMAGE_PATH),
            new ImagedTextCardDto("Канеки Кен", "Гуль тру SSS плюс банана ранга", MocksDefines.TEST_IMAGE_PATH),
            new ImagedTextCardDto("Петр Ильич Чайковский", "В прошлом битмейкер, но нашел себя в нашел", MocksDefines.TEST_IMAGE_PATH)
        );
    }

    @Override
    public List<IImagedTextCard> getPhotographers() {
        return equipment;
    }
    
}
