package php.runtime.exceptions;

import php.runtime.env.TraceInfo;
import php.runtime.exceptions.support.ErrorException;
import php.runtime.exceptions.support.ErrorType;

public class CustomErrorException extends ErrorException {
    protected final ErrorType type;
    public CustomErrorException(ErrorType type, String message, TraceInfo traceInfo) {
        super(message, traceInfo);
        this.type = type;
    }

    @Override
    public ErrorType getType() {
        return type;
    }
}
