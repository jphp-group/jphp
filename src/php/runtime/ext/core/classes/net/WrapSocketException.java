package php.runtime.ext.core.classes.net;

import php.runtime.env.Environment;
import php.runtime.ext.java.JavaException;
import php.runtime.reflection.ClassEntity;

import static php.runtime.annotation.Reflection.Name;

@Name("php\\net\\SocketException")
public class WrapSocketException extends JavaException {
    public WrapSocketException(Environment env, Throwable throwable) {
        super(env, throwable);
    }

    public WrapSocketException(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }
}
