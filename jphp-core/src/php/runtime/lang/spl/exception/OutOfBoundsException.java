package php.runtime.lang.spl.exception;

import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

import static php.runtime.annotation.Reflection.Name;

@Name("OutOfBoundsException")
public class OutOfBoundsException extends RuntimeException {
    public OutOfBoundsException(Environment env) {
        super(env);
    }

    public OutOfBoundsException(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }
}
