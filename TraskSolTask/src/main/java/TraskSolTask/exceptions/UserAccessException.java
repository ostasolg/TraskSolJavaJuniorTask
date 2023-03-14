package TraskSolTask.exceptions;

/**
 * Signifies that user has no access to required data.
 */
public class UserAccessException extends MainException {

    public UserAccessException(String message) {
        super(message);
    }
}

