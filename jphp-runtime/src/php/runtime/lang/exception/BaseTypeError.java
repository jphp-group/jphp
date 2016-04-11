package php.runtime.lang.exception;

import php.runtime.annotation.Reflection.Name;
import php.runtime.env.Environment;
import php.runtime.exceptions.support.ErrorType;
import php.runtime.reflection.ClassEntity;

@Name("TypeError")
public class BaseTypeError extends BaseError {
    public BaseTypeError(Environment env, ErrorType errorType) {
        super(env, errorType);
    }

    public BaseTypeError(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }
}
