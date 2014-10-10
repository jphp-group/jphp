package php.runtime.lang.spl.exception;

import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

import static php.runtime.annotation.Reflection.Name;

@Name("UnderflowException")
public class UnderflowException extends RuntimeException {
    public UnderflowException(Environment env) {
        super(env);
    }

    public UnderflowException(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }
}
