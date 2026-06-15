package pi.focus.server.core.service.api;

import java.util.UUID;

/**
 * Интерфейс-обертка для передачи идентификатора заказа.
 * Обычно используется в DTO при получении запросов на действия с конкретным заказом 
 */
public interface IOrderId {
    /**
     * Возвращает уникальный идентификатор заказа.
     * 
     * @return UUID заказа
     */
    UUID getOrderId();
}
