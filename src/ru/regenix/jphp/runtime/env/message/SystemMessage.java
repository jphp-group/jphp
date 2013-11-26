package ru.regenix.jphp.runtime.env.message;

import ru.regenix.jphp.common.Messages;
import ru.regenix.jphp.exceptions.support.ErrorException;
import ru.regenix.jphp.runtime.env.CallStackItem;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.env.TraceInfo;

abstract public class SystemMessage {
    private final Messages.Item message;
    private final Object[] args;
    private final CallStackItem trace;

    public SystemMessage(CallStackItem trace, Messages.Item message, Object... args){
        this.message = message;
        this.args = args;
        this.trace = trace;
    }

    public SystemMessage(Environment environment, Messages.Item message, Object... args){
        this.message = message;
        this.args = args;
        this.trace = environment.peekCall(0);
    }

    public CallStackItem getTrace() {
        return trace;
    }

    public String getMessage(){
        return message.fetch(args);
    }

    public String getDebugMessage(){
        return getType().getTypeName() + ": " + getMessage()
                + " in '" + trace.trace.getFileName() + "'"
                + " on line " + (trace.trace.getStartLine() + 1)
                + " at pos " + (trace.trace.getStartPosition() + 1);
    }

    abstract public ErrorException.Type getType();
}
