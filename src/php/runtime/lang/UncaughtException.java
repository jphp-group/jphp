package php.runtime.lang;

public class UncaughtException extends RuntimeException {
    protected BaseException exception;

    public UncaughtException(BaseException exception){
        this.exception = exception;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

    public BaseException getException() {
        return exception;
    }
}
