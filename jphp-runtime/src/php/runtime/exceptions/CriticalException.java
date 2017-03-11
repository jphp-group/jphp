package php.runtime.exceptions;

public class CriticalException extends RuntimeException implements JPHPException {

    public CriticalException(Throwable cause) {
        super(cause);
    }

    public CriticalException(String message) {
        super(message);
    }

    public CriticalException(String message, Throwable cause) {
        super(message, cause);
    }
}
