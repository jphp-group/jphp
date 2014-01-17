package php.runtime.env.handler;

import php.runtime.Memory;
import php.runtime.env.Environment;

abstract public class ConfigChangeHandler {
    abstract public void onChange(Environment env, Memory value);
}
