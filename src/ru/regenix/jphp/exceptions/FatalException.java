package ru.regenix.jphp.exceptions;

import ru.regenix.jphp.exceptions.support.ErrorException;
import ru.regenix.jphp.exceptions.support.ErrorType;
import ru.regenix.jphp.runtime.env.Context;
import ru.regenix.jphp.runtime.env.TraceInfo;

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
