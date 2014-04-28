package php.runtime.ext.core.classes;

import php.runtime.env.Environment;
import php.runtime.ext.java.JavaException;
import php.runtime.reflection.ClassEntity;

final public class WrapJavaExceptions {

    public static class IllegalArgumentException extends JavaException {
        public IllegalArgumentException(Environment env, Throwable throwable) {
            super(env, throwable);
        }

        public IllegalArgumentException(Environment env, ClassEntity clazz) {
            super(env, clazz);
        }
    }

    public static class IllegalStateException extends JavaException {
        public IllegalStateException(Environment env, Throwable throwable) {
            super(env, throwable);
        }

        public IllegalStateException(Environment env, ClassEntity clazz) {
            super(env, clazz);
        }
    }

    public static class NumberFormatException extends JavaException {
        public NumberFormatException(Environment env, Throwable throwable) {
            super(env, throwable);
        }

        public NumberFormatException(Environment env, ClassEntity clazz) {
            super(env, clazz);
        }
    }
}
