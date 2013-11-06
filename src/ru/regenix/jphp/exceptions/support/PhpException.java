package ru.regenix.jphp.exceptions.support;

import ru.regenix.jphp.runtime.env.Context;
import ru.regenix.jphp.runtime.env.TraceInfo;

abstract public class PhpException extends RuntimeException {

    protected final TraceInfo traceInfo;

    public PhpException(String message, TraceInfo traceInfo) {
        super(message);
        this.traceInfo = traceInfo;
    }

    public PhpException(String message, Context context){
        super(message);
        this.traceInfo = new TraceInfo(context);
    }

    public TraceInfo getTraceInfo() {
        return traceInfo;
    }
}
