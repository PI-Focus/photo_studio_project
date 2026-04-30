package pi.focus.server.api.context;

import java.util.List;

import pi.focus.server.api.models.IImagedTab;

public interface IPhotoroomsContext {
    // TODO: Есть ли резон заводить для этого другой класс? Те же карточки что в Imaged-Router'e, только поле названия вкладки не используется.
    // Однако, их можно использовать как айдишники в случае чего. Ток они должны быть уникальными
    public List<IImagedTab> getItems();
}
