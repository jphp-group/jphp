package php.runtime.env.handler;

import php.runtime.env.CompileScope;

abstract public class EntityFetchHandler {
    abstract public void fetch(CompileScope scope, String name, String lowerName);
}
