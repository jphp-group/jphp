package php.runtime.lang.spl.exception;

import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

import static php.runtime.annotation.Reflection.Name;

@Name("BadFunctionCallException")
public class BadFunctionCallException extends LogicException {
    public BadFunctionCallException(Environment env) {
        super(env);
    }

    public BadFunctionCallException(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }
}
