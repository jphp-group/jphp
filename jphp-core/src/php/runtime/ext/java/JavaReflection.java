package php.runtime.ext.java;

import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.exceptions.CriticalException;
import php.runtime.lang.BaseObject;
import php.runtime.reflection.ClassEntity;

import java.lang.reflect.InvocationTargetException;

@Reflection.Name("php\\lang\\JavaReflection")
abstract public class JavaReflection extends BaseObject {
    public JavaReflection(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public static void exception(Environment env, Throwable e) {
        Class tmp = e.getClass();
        Class<? extends JavaException> clazz = null;
        while (tmp != null) {
            clazz = env.scope.findJavaException(tmp);
            if (clazz != null)
                break;

            tmp = tmp.getSuperclass();
        }

        JavaException exception;
        if (clazz == null) {
            exception = new JavaException(env, e);
        } else {
            try {
                exception = clazz.getConstructor(Environment.class, Throwable.class).newInstance(env, e);
            } catch (InvocationTargetException e1) {
                throw new CriticalException(e1.getCause());
            } catch (Exception e1) {
                throw new CriticalException(e1);
            }
        }
        env.__throwException(exception);
    }
}
