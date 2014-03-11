package php.runtime.lang;

import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

import static php.runtime.annotation.Reflection.Name;

@Name("stdClass")
public class StdClass extends BaseObject {
    public StdClass(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public StdClass(Environment env) {
        super(env);
    }
}
