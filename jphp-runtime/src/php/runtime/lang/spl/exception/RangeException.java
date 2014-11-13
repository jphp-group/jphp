package php.runtime.lang.spl.exception;

import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

import static php.runtime.annotation.Reflection.Name;

@Name("RangeException")
public class RangeException extends RuntimeException {
    public RangeException(Environment env) {
        super(env);
    }

    public RangeException(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }
}
