package php.runtime.env.handler;

import php.runtime.Memory;
import php.runtime.env.Environment;

@FunctionalInterface
public interface ConfigChangeHandler {
    void onChange(Environment env, Memory value);
}
