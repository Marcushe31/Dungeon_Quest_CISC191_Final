package exception;

/**
 * Thrown when a skill cannot be paid for with the current mana or stamina.
 */
public class InsufficientResourceException extends RuntimeException {
    public InsufficientResourceException(String message) {
        super(message);
    }
}
