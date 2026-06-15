package pi.focus.server.core.exception;

import java.io.Serial;

/**
 * Исключение, выбрасываемое при ошибках загрузки статических данных приложения.
 * Используется, когда невозможно прочитать или обработать файлы начальной конфигурации.
 */
public class StaticDataLoadingException extends RuntimeException {
    /** Идентификатор версии для механизмов сериализации */
    @Serial
    private static final long serialVersionUID = 1;

    /**
     * Создает новое исключение.
     * 
     * @param message подробное описание причины ошибки
     * @param cause исходное исключение 
     */
    public StaticDataLoadingException(String message, Throwable cause) {
        super(message, cause);
    }
}