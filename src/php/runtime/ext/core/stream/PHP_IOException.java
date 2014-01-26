package php.runtime.ext.core.stream;

import php.runtime.env.Environment;
import php.runtime.lang.BaseException;
import php.runtime.reflection.ClassEntity;

import static php.runtime.annotation.Reflection.*;

@Name("php\\io\\IOException")
public class PHP_IOException extends BaseException {
    public PHP_IOException(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }
}
