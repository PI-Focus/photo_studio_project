package pi.focus.server.core.service.api;

import pi.focus.server.api.context.IPhotographersContext;

/**
 * Интерфейс сервиса для работы с командой фотографов студии.
 */
public interface IPhotographerService {
    /**
     * Возвращает данные о фотографах для формирования страницы каталога.
     * @return контекст со списком фотографов
     */
    IPhotographersContext getEquipmentContext();
}
