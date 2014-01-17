package ru.regenix.jphp.exceptions;

import ru.regenix.jphp.exceptions.support.ErrorType;
import php.runtime.env.Context;
import php.runtime.env.TraceInfo;
import ru.regenix.jphp.exceptions.support.ErrorException;

public class CompileException extends ErrorException {

    public CompileException(String message, TraceInfo traceInfo) {
        super(message, traceInfo);
    }

    public CompileException(String message, Context context) {
        super(message, context);
    }

    @Override
    public ErrorType getType() {
        return ErrorType.E_COMPILE_ERROR;
    }
}
