package pi.focus.server.core.service.api;

import pi.focus.server.api.context.IBaseContext;
import pi.focus.server.api.context.IInfoContext;

/**
 * Интерфейс сервиса для доступа к статическим данным сайта.
 * Обеспечивает получение информации о компании, правилах студии и базовых настройках (лого, контакты).
 */
public interface IStaticDataService {
    /**
     * @return данные для информационной страницы "О нас" и правил аренды
     */
    IInfoContext getInfo();
    /**
     * @return базовый контекст приложения (контакты, адреса, ссылки)
     */
    IBaseContext getBaseContext();
}
