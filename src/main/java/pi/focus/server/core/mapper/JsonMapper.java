package pi.focus.server.core.mapper;

import tools.jackson.databind.ObjectMapper;

/**
 * Маппер для работы с форматом JSON.
 * Предоставляет доступ к единому экземпляру для всего приложения.
 */
public final class JsonMapper {
    /** Глобальный экземпляр объекта для сериализации/десериализации JSON */
    private static final ObjectMapper INSTANCE = new ObjectMapper();

    /** Закрытый конструктор */
    private JsonMapper() {
    }

    /**
     * Возвращает единственный экземпляр ObjectMapper.
     * 
     * @return объект ObjectMapper
     */
    public static ObjectMapper getInstance() {
        return INSTANCE;
    }
}
