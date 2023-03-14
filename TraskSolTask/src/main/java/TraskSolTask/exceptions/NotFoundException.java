package TraskSolTask.exceptions;

/**
 * Indicates that a resource was not found.
 */
public class NotFoundException extends MainException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public static NotFoundException create(String resourceName, Object identifier) {

        return new NotFoundException(resourceName + " identified by " + identifier + " not found.");
    }

    public static NotFoundException create(String resourceName, Object firstIdentifier, Object secondIdentifier) {

        return new NotFoundException(resourceName + " identified by " + firstIdentifier +
                " and by " + secondIdentifier + " not found.");
    }
}
