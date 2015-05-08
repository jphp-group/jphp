package php.runtime.lang.exception;

import php.runtime.annotation.Reflection.Name;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Name("ParseException")
public class BaseParseException extends BaseBaseException {
    public BaseParseException(Environment env) {
        super(env);
    }

    public BaseParseException(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }
}
