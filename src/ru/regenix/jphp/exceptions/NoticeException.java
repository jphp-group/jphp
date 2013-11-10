package ru.regenix.jphp.exceptions;

import ru.regenix.jphp.exceptions.support.ErrorException;
import ru.regenix.jphp.runtime.env.Context;
import ru.regenix.jphp.runtime.env.TraceInfo;

public class NoticeException extends ErrorException {

    public NoticeException(String message, TraceInfo traceInfo) {
        super(message, traceInfo);
    }

    public NoticeException(String message, Context context) {
        super(message, context);
    }

    @Override
    public Type getType() {
        return Type.E_NOTICE;
    }
}
