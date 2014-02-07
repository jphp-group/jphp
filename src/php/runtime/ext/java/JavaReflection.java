package php.runtime.ext.java;

import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.reflection.ClassEntity;

@Reflection.Name("php\\lang\\JavaReflection")
abstract public class JavaReflection extends BaseObject {
    public JavaReflection(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public static void exception(Environment env, Throwable e){
        JavaException exception = new JavaException(env, env.fetchClass("php\\lang\\JavaException"));
        exception.setThrowable(e);
        env.__throwException(exception);
    }
}
