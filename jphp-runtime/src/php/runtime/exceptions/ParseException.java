package php.runtime.exceptions;

import php.runtime.env.TraceInfo;
import php.runtime.exceptions.support.ErrorException;
import php.runtime.exceptions.support.ErrorType;

public class ParseException extends ErrorException {
    public ParseException(String message, TraceInfo traceInfo) {
        super(message, traceInfo);
    }

    @Override
    public ErrorType getType() {
        return ErrorType.E_PARSE;
    }
}
