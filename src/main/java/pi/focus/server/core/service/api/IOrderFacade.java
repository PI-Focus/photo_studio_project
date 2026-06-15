package pi.focus.server.core.service.api;

import pi.focus.server.api.models.IOrderStatus;

import java.util.UUID;

/**
 * Фасад для управления процессами оформления заказов.
 * Инкапсулирует сложную бизнес-логику по валидации, созданию бронирований
 * и инициализации состояний заказа, координируя работу различных внутренних сервисов.
 */
public interface IOrderFacade {
    /**
     * Создает и возвращает новый пустой объект статуса заказа.
     * Используется для начальной инициализации процесса бронирования в сессии.
     * 
     * @return пустой объект реализации IOrderStatus
     */
    IOrderStatus getEmptyOrderStatus();
    /**
     * Выполняет комплексную проверку корректности данных в заказе.
     * Проверяет доступность времени, залов, оборудования и фотографов.
     * 
     * @param orderStatus объект с текущими данными заказа
     * @return код статуса валидации 
     */
    Integer validateOrderStatus(IOrderStatus orderStatus);
    /**
     * Регистрирует новое бронирование в системе для указанного пользователя.
     * 
     * @param id уникальный идентификатор пользователя (UUID), совершающего заказ
     * @param orderStatus подтвержденные данные заказа для сохранения
     */
    void createReservation(UUID id, IOrderStatus orderStatus);
}
