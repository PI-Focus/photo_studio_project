package pi.focus.server.core.json;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class JsonMapper {
    private static final ObjectMapper INSTANCE = new ObjectMapper();

    private JsonMapper() {
    }

    public static ObjectMapper getInstance() {
        return INSTANCE;
    }
}
