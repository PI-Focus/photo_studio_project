package pi.focus.server.core.exception;

import java.io.Serial;

public class StaticDataLoadingException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1;

    public StaticDataLoadingException(String message, Throwable cause) {
        super(message, cause);
    }
}