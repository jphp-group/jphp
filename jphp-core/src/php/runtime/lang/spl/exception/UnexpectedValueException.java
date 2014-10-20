package php.runtime.lang.spl.exception;

import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

import static php.runtime.annotation.Reflection.Name;

@Name("UnexpectedValueException")
public class UnexpectedValueException extends RuntimeException {
    public UnexpectedValueException(Environment env) {
        super(env);
    }

    public UnexpectedValueException(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }
}
