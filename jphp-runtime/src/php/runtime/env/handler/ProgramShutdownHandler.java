package php.runtime.env.handler;

import php.runtime.env.CompileScope;
import php.runtime.env.Environment;

abstract public class ProgramShutdownHandler {
    abstract public void onShutdown(CompileScope scope, Environment env);
}
