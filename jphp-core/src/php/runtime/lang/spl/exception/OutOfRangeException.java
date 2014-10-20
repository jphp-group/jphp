package php.runtime.lang.spl.exception;

import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

import static php.runtime.annotation.Reflection.Name;

@Name("OutOfRangeException")
public class OutOfRangeException extends RuntimeException {
    public OutOfRangeException(Environment env) {
        super(env);
    }

    public OutOfRangeException(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }
}
