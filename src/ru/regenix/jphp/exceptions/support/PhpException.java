package ru.regenix.jphp.exceptions.support;

import ru.regenix.jphp.exceptions.JPHPException;
import php.runtime.env.Context;
import php.runtime.env.TraceInfo;

abstract public class PhpException extends RuntimeException implements JPHPException {

    protected TraceInfo traceInfo;

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

    public void setTraceInfo(TraceInfo traceInfo) {
        this.traceInfo = traceInfo;
    }
}
