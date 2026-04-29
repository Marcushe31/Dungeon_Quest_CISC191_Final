package exception;

/**
 * Wraps file I/O errors with a project-specific exception type.
 */
public class SaveLoadException extends RuntimeException {
    public SaveLoadException(String message, Throwable cause) {
        super(message, cause);
    }

    public SaveLoadException(String message) {
        super(message);
    }
}
