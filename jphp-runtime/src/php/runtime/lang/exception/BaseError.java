package php.runtime.lang.exception;

import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.BaseType;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.common.HintType;
import php.runtime.common.Modifier;
import php.runtime.env.Environment;
import php.runtime.exceptions.support.ErrorType;
import php.runtime.lang.BaseException;
import php.runtime.lang.exception.BaseBaseException;
import php.runtime.memory.LongMemory;
import php.runtime.reflection.ClassEntity;

@BaseType
@Name("Error")
@Signature(root = true, value =
        {
                @Reflection.Arg(value = "message", modifier = Modifier.PROTECTED, type = HintType.STRING),
                @Reflection.Arg(value = "code", modifier = Modifier.PROTECTED, type = HintType.INT),
                @Reflection.Arg(value = "previous", modifier = Modifier.PROTECTED, type = HintType.OBJECT),
                @Reflection.Arg(value = "trace", modifier = Modifier.PROTECTED, type = HintType.ARRAY),
                @Reflection.Arg(value = "file", modifier = Modifier.PROTECTED, type = HintType.STRING),
                @Reflection.Arg(value = "line", modifier = Modifier.PROTECTED, type = HintType.INT),
                @Reflection.Arg(value = "position", modifier = Modifier.PROTECTED, type = HintType.INT)
        }
)
public class BaseError  extends BaseBaseException implements BaseThrowable {
    protected ErrorType errorType = ErrorType.E_CORE_ERROR;

    public BaseError(Environment env) {
        super(env);
    }

    public BaseError(Environment env, ErrorType errorType) {
        super(env);
        this.errorType = errorType;
    }

    public BaseError(Environment env, ClassEntity clazz) {
        super(env, clazz);
        errorType = ErrorType.E_ERROR;
    }

    @Signature
    public Memory getErrorType() {
        return LongMemory.valueOf(errorType.value);
    }

    @Signature({
            @Reflection.Arg(value = "message", optional = @Reflection.Optional(value = "", type = HintType.STRING)),
            @Reflection.Arg(value = "code", optional = @Reflection.Optional(value = "0", type = HintType.INT)),
            @Reflection.Arg(value = "previous", nativeType = BaseThrowable.class, optional = @Reflection.Optional(value = "NULL"))
    })
    public Memory __construct(Environment env, Memory... args) {
        clazz.refOfProperty(props, "message").assign(args[0].toString());
        if (args.length > 1)
            clazz.refOfProperty(props, "code").assign(args[1].toLong());

        if (args.length > 2)
            clazz.refOfProperty(props, "previous").assign(args[2]);

        return Memory.NULL;
    }

    @Override
    @Signature
    final public Memory getMessage(Environment env, Memory... args) {
        return super.getMessage(env, args);
    }

    @Override
    @Signature
    final public Memory getCode(Environment env, Memory... args) {
        return super.getCode(env, args);
    }

    @Override
    @Signature
    final public Memory getLine(Environment env, Memory... args) {
        return super.getLine(env, args);
    }

    @Override
    @Signature
    final public Memory getPosition(Environment env, Memory... args) {
        return super.getPosition(env, args);
    }

    @Override
    @Signature
    final public Memory getFile(Environment env, Memory... args) {
        return super.getFile(env, args);
    }

    @Override
    @Signature
    final public Memory getTrace(Environment env, Memory... args) {
        return super.getTrace(env, args);
    }

    @Override
    @Signature
    final public Memory getPrevious(Environment env, Memory... args) {
        return super.getPrevious(env, args);
    }

    @Override
    @Signature
    public Memory __toString(Environment env, Memory... args) {
        return super.__toString(env, args);
    }

    @Override
    @Signature
    final public Memory getTraceAsString(Environment env, Memory... args) {
        return super.getTraceAsString(env, args);
    }

    @Signature
    private Memory __clone(Environment env, Memory... args) {
        return Memory.NULL;
    }
}
