package ru.regenix.jphp.exceptions;

import ru.regenix.jphp.env.TraceInfo;

public class PHPException extends RuntimeException {

    protected final TraceInfo traceInfo;

    public PHPException(String message, TraceInfo traceInfo) {
        super(message);
        this.traceInfo = traceInfo;
    }

    public TraceInfo getTraceInfo() {
        return traceInfo;
    }
}
