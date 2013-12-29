package ru.regenix.jphp.exceptions;

import ru.regenix.jphp.exceptions.support.ErrorException;
import ru.regenix.jphp.exceptions.support.ErrorType;
import ru.regenix.jphp.runtime.env.TraceInfo;

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
