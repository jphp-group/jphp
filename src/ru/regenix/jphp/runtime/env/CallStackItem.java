package ru.regenix.jphp.runtime.env;

import ru.regenix.jphp.runtime.lang.PHPObject;
import ru.regenix.jphp.runtime.memory.support.Memory;

public class CallStackItem {
    public TraceInfo trace;
    public PHPObject object;
    public Memory[] args;

    public String function;
    public String clazz;

    public CallStackItem(TraceInfo trace) {
        this.trace = trace;
    }

    public CallStackItem(TraceInfo trace, PHPObject object, Memory[] args, String function, String clazz) {
        this.trace = trace;
        this.object = object;
        this.args = args;
        this.function = function;
        this.clazz = clazz;
    }

    public void setParameters(TraceInfo trace, PHPObject object, Memory[] args, String function, String clazz) {
        this.trace = trace;
        this.object = object;
        this.args = args;
        this.function = function;
        this.clazz = clazz;
    }

    public void clear(){
        this.object = null;
        this.args = null;
    }
}
