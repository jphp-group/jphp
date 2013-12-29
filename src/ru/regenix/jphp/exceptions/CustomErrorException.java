package ru.regenix.jphp.exceptions;

import ru.regenix.jphp.exceptions.support.ErrorException;
import ru.regenix.jphp.runtime.env.TraceInfo;

public class CustomErrorException extends ErrorException {
    protected final Type type;
    public CustomErrorException(Type type, String message, TraceInfo traceInfo) {
        super(message, traceInfo);
        this.type = type;
    }

    @Override
    public Type getType() {
        return type;
    }
}
