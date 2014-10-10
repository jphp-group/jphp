package php.runtime.lang.spl.exception;

import php.runtime.env.Environment;
import php.runtime.lang.BaseException;
import php.runtime.reflection.ClassEntity;

import static php.runtime.annotation.Reflection.Name;

@Name("LogicException")
public class LogicException extends BaseException {
    public LogicException(Environment env) {
        super(env);
    }

    public LogicException(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }
}
