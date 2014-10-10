package php.runtime.lang.spl.exception;

import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

import static php.runtime.annotation.Reflection.Name;

@Name("OverflowException")
public class OverflowException extends RuntimeException {
    public OverflowException(Environment env) {
        super(env);
    }

    public OverflowException(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }
}
