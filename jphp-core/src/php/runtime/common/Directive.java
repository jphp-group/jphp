package php.runtime.common;

import php.runtime.env.TraceInfo;

public class Directive {
    public final String value;
    public final TraceInfo trace;

    public Directive(String value, TraceInfo traceInfo) {
        this.value = value;
        this.trace = traceInfo;
    }
}
