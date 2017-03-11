package php.runtime.ext.java;

import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.exceptions.CriticalException;
import php.runtime.lang.BaseObject;
import php.runtime.reflection.ClassEntity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Reflection.Name("php\\lang\\JavaReflection")
abstract public class JavaReflection extends BaseObject {
    protected static Map<Class<? extends JavaException>, Constructor<? extends JavaException>> constructors;
    protected static Map<Class<? extends Throwable>, Class<? extends JavaException>> cachedThClasses;

    public JavaReflection(Environment env) {
        super(env);
    }

    public JavaReflection(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public static void exception(Environment env, Throwable e) {
        if (cachedThClasses == null)
            cachedThClasses = new ConcurrentHashMap<Class<? extends Throwable>, Class<? extends JavaException>>();

        if (constructors == null)
            constructors = new ConcurrentHashMap<Class<? extends JavaException>, Constructor<? extends JavaException>>();

        Class tmp = e.getClass();
        Class<? extends JavaException> clazz = cachedThClasses.get(tmp);
        if (clazz == null) {
            while (tmp != null) {
                clazz = env.scope.findJavaException(tmp);
                if (clazz != null) {
                    cachedThClasses.put(e.getClass(), clazz);
                    break;
                }

                tmp = tmp.getSuperclass();
            }
        }

        if (clazz == null) {
            ClassEntity context = env.getLastClassOnStack();

            while (context != null) {
                clazz = env.scope.findJavaExceptionForContext(context.getLowerName());
                if (clazz != null)
                    break;

                context = context.getParent();
            }
        }

        JavaException exception;
        if (clazz == null) {
            exception = new JavaException(env, e);
        } else {
            try {
                Constructor<? extends JavaException> constructor = constructors.get(clazz);
                if (constructor == null) {
                    constructors.put(clazz, constructor = clazz.getConstructor(Environment.class, Throwable.class));
                }

                exception = constructor.newInstance(env, e);
            } catch (InvocationTargetException e1) {
                throw new CriticalException(e1.getCause());
            } catch (Exception e1) {
                throw new CriticalException(e1);
            }
        }
        env.__throwException(exception, false);
    }
}
