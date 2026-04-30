package pi.focus.server.service.context.mocks;

import java.util.List;

import pi.focus.server.api.context.IPhotoroomsContext;
import pi.focus.server.api.models.IImagedTab;
import pi.focus.server.service.models.mocks.ImagedTabMock;

public class PhotoroomsContextMock implements IPhotoroomsContext{
    private List<IImagedTab> items;

    public PhotoroomsContextMock() {
        items = List.of(
            new ImagedTabMock("Крутой зал 1", "НОУ ВЕЙ этот зал просто ИМБА", null, "зал1", "photorooms/1"),
            new ImagedTabMock("Невероятный зал 2", "НОУ ВЕЙ этот зал просто зал!", null, "зал2", "photorooms/2"),
            new ImagedTabMock("Чёткий зал 3", "НОУ ВЕЙ этот зал просто биИМБА", null, "зал3", "photorooms/3"),
            new ImagedTabMock("Такой себе зал 4", "НОУ ВЕЙ этот зал просто НЕ ИМБА", null, "зал4", "photorooms/4")
        );
    }

    @Override
    public List<IImagedTab> getItems() {
        return items;
    }
    
}
