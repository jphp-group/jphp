package php.runtime.ext.core.classes;

import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

@Name("php\\lang\\ClassLoader")
public class WrapClassLoader extends BaseWrapper<ClassLoader> {
    public WrapClassLoader(Environment env, ClassLoader wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapClassLoader(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    private void __construct() { }

    @Signature
    public void loadClass(Environment env, String className) {
        env.fetchClass(className, false);
    }
}
