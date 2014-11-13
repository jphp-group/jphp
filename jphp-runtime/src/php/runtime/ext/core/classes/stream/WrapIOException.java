package php.runtime.ext.core.classes.stream;

import php.runtime.env.Environment;
import php.runtime.ext.java.JavaException;
import php.runtime.reflection.ClassEntity;

import static php.runtime.annotation.Reflection.Name;

@Name("php\\io\\IOException")
public class WrapIOException extends JavaException {
    public WrapIOException(Environment env, Throwable throwable) {
        super(env, throwable);
    }

    public WrapIOException(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }
}
