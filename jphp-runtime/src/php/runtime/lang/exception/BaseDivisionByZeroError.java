package php.runtime.lang.exception;

import php.runtime.annotation.Reflection.Name;
import php.runtime.env.Environment;
import php.runtime.exceptions.support.ErrorType;
import php.runtime.reflection.ClassEntity;

@Name("DivisionByZeroError")
public class BaseDivisionByZeroError extends BaseArithmeticError {
    public BaseDivisionByZeroError(Environment env, ErrorType errorType) {
        super(env, errorType);
    }

    public BaseDivisionByZeroError(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }
}
