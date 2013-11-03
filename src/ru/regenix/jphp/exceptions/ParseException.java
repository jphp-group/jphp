package ru.regenix.jphp.exceptions;

import ru.regenix.jphp.env.TraceInfo;
import ru.regenix.jphp.exceptions.support.ErrorException;

public class ParseException extends ErrorException {
    public ParseException(String message, TraceInfo traceInfo) {
        super(message, traceInfo);
    }

    @Override
    public Type getType() {
        return Type.E_PARSE;
    }
}
