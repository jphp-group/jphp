package php.runtime.env.handler;

import php.runtime.env.CompileScope;

@FunctionalInterface
public interface EntityFetchHandler {
    void fetch(CompileScope scope, String name, String lowerName);
}
