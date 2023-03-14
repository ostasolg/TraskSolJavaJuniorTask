package TraskSolTask.exceptions;


/**
 * Base for all application-specific exceptions.
 */
public class MainException extends RuntimeException {

    public MainException() {
    }

    public MainException(String message) {
        super(message);
    }

    public MainException(String message, Throwable cause) {
        super(message, cause);
    }

    public MainException(Throwable cause) {
        super(cause);
    }
}
