package php.runtime.launcher;

public class LaunchException extends RuntimeException {
    public LaunchException(Throwable cause) {
        super(cause);
    }

    public LaunchException(String message) {
        super(message);
    }
}
