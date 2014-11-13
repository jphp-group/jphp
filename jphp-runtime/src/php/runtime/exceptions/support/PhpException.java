package php.runtime.exceptions.support;

import php.runtime.env.Context;
import php.runtime.env.TraceInfo;
import php.runtime.exceptions.JPHPException;

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
        return traceInfo == null ? TraceInfo.UNKNOWN : traceInfo;
    }

    public void setTraceInfo(TraceInfo traceInfo) {
        this.traceInfo = traceInfo;
    }
}
