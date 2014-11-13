package php.runtime.lang.spl.exception;

import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

import static php.runtime.annotation.Reflection.Name;

@Name("BadMethodCallException")
public class BadMethodCallException extends BadFunctionCallException {
    public BadMethodCallException(Environment env) {
        super(env);
    }

    public BadMethodCallException(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }
}
