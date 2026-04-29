package exception;

/**
 * Thrown when a controller receives an action it cannot process.
 */
public class InvalidActionException extends RuntimeException {
    public InvalidActionException(String message) {
        super(message);
    }
}
