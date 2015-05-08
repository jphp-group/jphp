package php.runtime.env.handler;

import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;

abstract public class TickHandler {
    abstract public void onTick(Environment env, TraceInfo trace);
}
