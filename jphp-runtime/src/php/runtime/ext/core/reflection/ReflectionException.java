package php.runtime.ext.core.reflection;

import php.runtime.env.Environment;
import php.runtime.lang.BaseException;
import php.runtime.reflection.ClassEntity;

import static php.runtime.annotation.Reflection.*;

@Name("ReflectionException")
public class ReflectionException extends BaseException {
    public ReflectionException(Environment env) {
        super(env);
    }

    public ReflectionException(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }
}
