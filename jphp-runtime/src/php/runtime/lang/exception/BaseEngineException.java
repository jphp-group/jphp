package php.runtime.lang.exception;

import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.exceptions.support.ErrorType;
import php.runtime.lang.exception.BaseBaseException;
import php.runtime.memory.LongMemory;
import php.runtime.reflection.ClassEntity;

@Name("EngineException")
public class BaseEngineException extends BaseBaseException {
    protected final ErrorType errorType;

    public BaseEngineException(Environment env, ErrorType errorType) {
        super(env);
        this.errorType = errorType;
    }

    public BaseEngineException(Environment env, ClassEntity clazz) {
        super(env, clazz);
        errorType = ErrorType.E_ERROR;
    }

    @Signature
    public Memory getErrorType() {
        return LongMemory.valueOf(errorType.value);
    }
}
