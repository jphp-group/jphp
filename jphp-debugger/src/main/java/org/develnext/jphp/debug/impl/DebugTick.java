package org.develnext.jphp.debug.impl;

import org.develnext.jphp.debug.impl.breakpoint.Breakpoint;
import php.runtime.env.CallStack;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.ArrayMemory;

public class DebugTick {
    protected final Breakpoint breakpoint;
    protected final Environment environment;
    protected final TraceInfo trace;
    protected final ArrayMemory locals;

    protected CallStack callStack;

    public DebugTick(Breakpoint breakpoint, Environment environment, TraceInfo trace, ArrayMemory locals) {
        this.breakpoint = breakpoint;
        this.environment = environment;
        this.trace = trace;
        this.locals = locals;

        this.callStack = environment == null ? null : environment.getCallStack().getSnapshotAsCallStack();
    }

    public Breakpoint getBreakpoint() {
        return breakpoint;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public TraceInfo getTrace() {
        return trace;
    }

    public ArrayMemory getLocals() {
        return locals;
    }

    public CallStack getCallStack() {
        return callStack;
    }
}
