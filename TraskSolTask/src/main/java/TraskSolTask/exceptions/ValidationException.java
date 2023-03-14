package TraskSolTask.exceptions;


/**
 * Signifies that invalid data have been provided to the application.
 */
public class ValidationException extends MainException {

    public ValidationException(String message) {
        super(message);
    }
}
