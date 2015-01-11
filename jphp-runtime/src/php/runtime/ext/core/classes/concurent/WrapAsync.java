package php.runtime.ext.core.classes.concurent;

import php.runtime.annotation.Reflection.Name;
import php.runtime.env.Environment;
import php.runtime.ext.CoreExtension;
import php.runtime.lang.BaseObject;
import php.runtime.reflection.ClassEntity;

@Name(CoreExtension.NAMESPACE + "concurrent\\Async")
public class WrapAsync extends BaseObject {
    public WrapAsync(Environment env) {
        super(env);
    }

    public WrapAsync(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }
}
