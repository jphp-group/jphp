package php.runtime.exceptions;

import php.runtime.env.Context;
import php.runtime.env.TraceInfo;
import php.runtime.exceptions.support.ErrorException;
import php.runtime.exceptions.support.ErrorType;

public class FatalException extends ErrorException {

    public FatalException(String message, TraceInfo traceInfo) {
        super(message, traceInfo);
    }

    public FatalException(String message, Context context) {
        super(message, context);
    }

    @Override
    public ErrorType getType() {
        return ErrorType.E_ERROR;
    }
}
