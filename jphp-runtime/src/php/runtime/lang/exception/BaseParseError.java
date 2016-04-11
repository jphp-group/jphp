package php.runtime.lang.exception;

import php.runtime.annotation.Reflection.Name;
import php.runtime.env.Environment;
import php.runtime.exceptions.support.ErrorType;
import php.runtime.reflection.ClassEntity;

@Name("ParseError")
public class BaseParseError extends BaseError {
    public BaseParseError(Environment env) {
        super(env);
    }

    public BaseParseError(Environment env, ErrorType errorType) {
        super(env, errorType);
    }

    public BaseParseError(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }
}
