package php.runtime.lang.spl.exception;

import php.runtime.env.Environment;
import php.runtime.lang.BaseException;
import php.runtime.reflection.ClassEntity;

import static php.runtime.annotation.Reflection.Name;

@Name("RuntimeException")
public class RuntimeException extends BaseException {
    public RuntimeException(Environment env) {
        super(env);
    }

    public RuntimeException(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }
}
