package php.runtime.lang.spl.exception;

import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

import static php.runtime.annotation.Reflection.Name;

@Name("InvalidArgumentException")
public class InvalidArgumentException extends LogicException {
    public InvalidArgumentException(Environment env) {
        super(env);
    }

    public InvalidArgumentException(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }
}
