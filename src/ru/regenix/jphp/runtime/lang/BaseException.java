package ru.regenix.jphp.runtime.lang;

import ru.regenix.jphp.common.HintType;
import ru.regenix.jphp.common.Modifier;
import ru.regenix.jphp.runtime.env.CallStackItem;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.env.TraceInfo;
import ru.regenix.jphp.runtime.memory.ArrayMemory;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.reflection.ClassEntity;

import static ru.regenix.jphp.runtime.annotation.Reflection.*;


@Name("Exception")
@Signature(root = true, value =
{
       @Arg(value = "message", modifier = Modifier.PROTECTED, type = HintType.STRING),
       @Arg(value = "code", modifier = Modifier.PROTECTED, type = HintType.NUMERIC),
       @Arg(value = "previous", modifier = Modifier.PROTECTED, type = HintType.OBJECT),
       @Arg(value = "trace", modifier = Modifier.PROTECTED, type = HintType.ARRAY),
       @Arg(value = "file", modifier = Modifier.PROTECTED, type = HintType.STRING),
       @Arg(value = "line", modifier = Modifier.PROTECTED, type = HintType.NUMERIC)
})
public class BaseException extends RuntimeException implements IObject {
    public final ArrayMemory __dynamicProperties__;
    public final ClassEntity __class__;
    public TraceInfo trace;
    public CallStackItem[] callStack;

    public BaseException(Environment env, ClassEntity clazz) {
        this.__class__ = clazz;
        this.__dynamicProperties__ = new ArrayMemory();
    }

    @Signature({
            @Arg(value = "message", optional = @Optional(value = "", type = HintType.STRING)),
            @Arg(value = "code", optional = @Optional(value = "0", type = HintType.NUMERIC)),
            @Arg(value = "previous", optional = @Optional(value = "NULL"))
    })
    public Memory __construct(Environment env, Memory... args) {
        __dynamicProperties__.refOfIndex("message").assign(args[0].toString());
        __dynamicProperties__.refOfIndex("code").assign(args[1].toLong());
        __dynamicProperties__.refOfIndex("previous").assign(args[2]);
        return Memory.NULL;
    }

    public void setTraceInfo(Environment env, TraceInfo trace){
        this.callStack = env.getCallStackSnapshot();
        this.trace = trace;

        if (trace != null){
            __dynamicProperties__.refOfIndex("line").assign(trace.getStartLine() + 1);
            __dynamicProperties__.refOfIndex("file").assign(trace.getFileName());
        }
    }

    @Signature
    final public Memory getLine(Environment env, Memory... args){
        return __dynamicProperties__.valueOfIndex("line");
    }

    @Signature
    final public Memory getFile(Environment env, Memory... args){
        return __dynamicProperties__.valueOfIndex("file");
    }

    @Signature
    final public Memory getTrace(Environment env, Memory... args){
        return __dynamicProperties__.valueOfIndex("trace");
    }

    @Override
    public ClassEntity getReflection() {
        return __class__;
    }

    @Override
    public ArrayMemory getProperties() {
        return __dynamicProperties__;
    }

    @Override
    final public int getPointer() {
        return super.hashCode();
    }

    /**
     * Since we override this method, no stacktrace is generated - much faster
     * @return always null
     */
    public Throwable fillInStackTrace() {
        return null;
    }
}
