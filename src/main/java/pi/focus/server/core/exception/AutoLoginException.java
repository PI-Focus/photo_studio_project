package pi.focus.server.core.exception;

import java.io.Serial;

public class AutoLoginException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1;

    public AutoLoginException(String message, Throwable cause) {
        super(message, cause);
    }
}
