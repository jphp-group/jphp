package php.runtime.env.handler;

import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.ArrayMemory;

@FunctionalInterface
public interface TickHandler {
    void onTick(Environment env, TraceInfo trace, ArrayMemory locals);
}
