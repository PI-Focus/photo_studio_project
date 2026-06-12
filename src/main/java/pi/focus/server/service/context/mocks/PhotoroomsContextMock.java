package pi.focus.server.service.context.mocks;

import java.util.List;

import pi.focus.server.api.context.IPhotoroomsContext;
import pi.focus.server.api.models.IDataCard;
import pi.focus.server.service.models.DataCardDto;

public class PhotoroomsContextMock implements IPhotoroomsContext {
    private final List<IDataCard> photorooms;

    public PhotoroomsContextMock() {
        photorooms = List.of(
            new DataCardDto("КРУТОЙ Зал 1", "Крутое описание зала 1", MocksDefines.TEST_IMAGE_PATH, "/photorooms/4f9b37a1-8d2c-4b6a-9f1e-3c5b7a9d2e4f"),
            new DataCardDto("ОФИГЕННЫЙ Зал 2", "Офигенное описание зала 2", MocksDefines.TEST_IMAGE_PATH, "/photorooms/a1b2c3d4-e5f6-7a8b-9c0d-1e2f3a4b5c6d"),
            new DataCardDto("милый Зал 3", "Милое описание зала 3", MocksDefines.TEST_IMAGE_PATH, "/photorooms/7d8e9f0a-1b2c-3d4e-5f6a-7b8c9d0e1f2a")
        );
    }

    @Override
    public List<IDataCard> getPhotorooms() {
        return photorooms;
    }
}
