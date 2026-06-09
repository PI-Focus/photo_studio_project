package pi.focus.server.service.context.mocks;

import java.util.List;

import pi.focus.server.api.context.IPhotoroomsContext;
import pi.focus.server.api.models.IDataCard;
import pi.focus.server.service.models.mocks.DataCardMock;

public class PhotoroomsContextMock implements IPhotoroomsContext {
    private final List<IDataCard> photorooms;

    public PhotoroomsContextMock() {
        photorooms = List.of(
            new DataCardMock("КРУТОЙ Зал 1", "Крутое описание зала 1", MocksDefines.TEST_IMAGE_PATH, "/photorooms/1"),
            new DataCardMock("ОФИГЕННЫЙ Зал 2", "Офигенное описание зала 2", MocksDefines.TEST_IMAGE_PATH, "/photorooms/2"),
            new DataCardMock("милый Зал 3", "Милое описание зала 3", MocksDefines.TEST_IMAGE_PATH, "/photorooms/3")
        );
    }

    @Override
    public List<IDataCard> getPhotorooms() {
        return photorooms;
    }
}
