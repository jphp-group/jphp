package php.runtime.lang;

import php.runtime.env.Environment;
import php.runtime.lang.exception.BaseBaseException;
import php.runtime.reflection.ClassEntity;

import static php.runtime.annotation.Reflection.*;

@Name("Exception")
public class BaseException extends BaseBaseException {
    public BaseException(Environment env) {
        super(env);
    }

    public BaseException(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }
}
