package php.runtime.lang;

import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.common.HintType;
import php.runtime.common.Modifier;
import php.runtime.env.CallStackItem;
import php.runtime.env.Environment;
import php.runtime.lang.exception.BaseBaseException;
import php.runtime.lang.exception.BaseThrowable;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

import static php.runtime.annotation.Reflection.*;

@BaseType
@Name("Exception")
@Reflection.Signature(root = true, value =
{
        @Arg(value = "message", modifier = Modifier.PROTECTED, type = HintType.STRING),
        @Arg(value = "code", modifier = Modifier.PROTECTED, type = HintType.INT),
        @Arg(value = "previous", modifier = Modifier.PROTECTED, type = HintType.OBJECT),
        @Arg(value = "trace", modifier = Modifier.PROTECTED, type = HintType.ARRAY),
        @Arg(value = "file", modifier = Modifier.PROTECTED, type = HintType.STRING),
        @Arg(value = "line", modifier = Modifier.PROTECTED, type = HintType.INT),
        @Arg(value = "position", modifier = Modifier.PROTECTED, type = HintType.INT)
})
public class BaseException extends BaseBaseException implements BaseThrowable {
    public BaseException(Environment env) {
        super(env);
    }

    public BaseException(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature(value = {
            @Arg(value = "message", optional = @Optional(value = "")),
            @Arg(value = "code", optional = @Optional(value = "0")),
            @Arg(value = "previous", nativeType = BaseThrowable.class, optional = @Optional(value = "NULL"))
    })
    public Memory __construct(Environment env, Memory... args) {
        clazz.refOfProperty(props, "message").assign(args[0].toString());
        if (args.length > 1) {
            clazz.refOfProperty(props, "code").assign(args[1].toLong());
        }

        if (args.length > 2) {
            clazz.refOfProperty(props, "previous").assign(args[2]);
        }

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
