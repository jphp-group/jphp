package ru.regenix.jphp.exceptions;

import ru.regenix.jphp.exceptions.support.ErrorType;
import php.runtime.env.TraceInfo;
import ru.regenix.jphp.exceptions.support.ErrorException;

public class ParseException extends ErrorException {
    public ParseException(String message, TraceInfo traceInfo) {
        super(message, traceInfo);
    }

    @Override
    public ErrorType getType() {
        return ErrorType.E_PARSE;
    }
}
