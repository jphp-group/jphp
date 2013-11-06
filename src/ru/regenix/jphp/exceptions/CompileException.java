package ru.regenix.jphp.exceptions;

import ru.regenix.jphp.runtime.env.Context;
import ru.regenix.jphp.runtime.env.TraceInfo;
import ru.regenix.jphp.exceptions.support.ErrorException;

public class CompileException extends ErrorException {

    public CompileException(String message, TraceInfo traceInfo) {
        super(message, traceInfo);
    }

    public CompileException(String message, Context context) {
        super(message, context);
    }

    @Override
    public Type getType() {
        return Type.E_COMPILE_ERROR;
    }
}
