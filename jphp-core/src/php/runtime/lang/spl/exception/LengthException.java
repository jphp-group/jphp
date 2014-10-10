package php.runtime.lang.spl.exception;

import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

import static php.runtime.annotation.Reflection.Name;

@Name("LengthException")
public class LengthException extends LogicException {
    public LengthException(Environment env) {
        super(env);
    }

    public LengthException(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }
}
