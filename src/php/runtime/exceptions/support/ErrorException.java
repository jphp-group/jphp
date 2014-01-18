package php.runtime.exceptions.support;

import php.runtime.env.Context;
import php.runtime.env.TraceInfo;

abstract public class ErrorException extends PhpException {

    public ErrorException(String message, TraceInfo traceInfo) {
        super(message, traceInfo);
    }

    public ErrorException(String message, Context context) {
        super(message, context);
    }

    abstract public ErrorType getType();
}
