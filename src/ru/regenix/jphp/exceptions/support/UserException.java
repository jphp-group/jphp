package ru.regenix.jphp.exceptions.support;

import ru.regenix.jphp.runtime.env.Context;
import ru.regenix.jphp.runtime.env.TraceInfo;

abstract public class UserException extends PhpException {
    public UserException(String message, TraceInfo traceInfo) {
        super(message, traceInfo);
    }

    public UserException(String message, Context context) {
        super(message, context);
    }
}
