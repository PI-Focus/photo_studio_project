package pi.focus.server.core.service.api;


import pi.focus.server.api.context.IEquipmentContext;

/**
 * Интерфейс сервиса для управления каталогом оборудования.
 */
public interface IEquipmentService {
    /**
     * Возвращает данные о доступном оборудовании для отображения в пользовательском интерфейсе.
     * @return контекст со списком оборудования
     */
    IEquipmentContext getEquipmentContext();
}
