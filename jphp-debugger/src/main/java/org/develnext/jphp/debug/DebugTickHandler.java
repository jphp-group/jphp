package org.develnext.jphp.debug;

import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.env.handler.TickHandler;

public class DebugTickHandler extends TickHandler {
    @Override
    public void onTick(Environment env, TraceInfo trace) {
        // TODO: Implement debugger.
    }
}
