package org.develnext.jphp.debug;

import org.develnext.jphp.debug.impl.DebugTick;
import org.develnext.jphp.debug.impl.Debugger;
import org.develnext.jphp.debug.impl.DebuggerException;
import org.develnext.jphp.debug.impl.breakpoint.Breakpoint;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.env.handler.TickHandler;
import php.runtime.memory.ArrayMemory;

public class DebugTickHandler implements TickHandler {
    protected Debugger debugger;
    protected boolean init = false;

    public void setDebugger(Debugger debugger) {
        this.debugger = debugger;
    }

    protected void waitDebugger() {
        while (debugger == null || debugger.isWorking()) {
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                throw new DebuggerException(e);
            }
        }
    }

    @Override
    public void onTick(Environment env, TraceInfo trace, ArrayMemory locals) {
        waitDebugger();

        if (!init) {
            init = true;
            debugger.registerBreak(null, env, trace, locals);
            return;
        }

        DebugTick oldTick = debugger.getRegisteredTick();

        Debugger.Step waitStep = debugger.getWaitStep();

        Breakpoint breakpoint = debugger.breakpointManager.findFor(env, trace);

        switch (waitStep) {
            case OVER:
                if (oldTick.getCallStack().getTop() >= env.getCallStackTop()) {
                    debugger.registerBreak(breakpoint, env, trace, locals);
                }

                break;

            case OUT:
                if (oldTick.getCallStack().getTop() > env.getCallStackTop()) {
                    debugger.registerBreak(breakpoint, env, trace, locals);
                }

                break;

            case INTO:
                debugger.registerBreak(breakpoint, env, trace, locals);
                break;

            case RUN:
                if (breakpoint != null) {
                    debugger.registerBreak(breakpoint, env, trace, locals);
                }
        }

        waitDebugger();
    }
}
