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
       @Arg(value = "code", modifier = Modifier.PROTECTED, type = HintType.INT),
       @Arg(value = "previous", modifier = Modifier.PROTECTED, type = HintType.OBJECT),
       @Arg(value = "trace", modifier = Modifier.PROTECTED, type = HintType.ARRAY),
       @Arg(value = "file", modifier = Modifier.PROTECTED, type = HintType.STRING),
       @Arg(value = "line", modifier = Modifier.PROTECTED, type = HintType.INT),
       @Arg(value = "position", modifier = Modifier.PROTECTED, type = HintType.INT)
})
public class BaseException extends RuntimeException implements IObject {
    protected final ArrayMemory __dynamicProperties__;
    protected final ClassEntity __class__;
    protected final Environment __env__;
    protected TraceInfo trace;
    protected CallStackItem[] callStack;

    private boolean init = true;

    public BaseException(Environment env, ClassEntity clazz) {
        this.__class__ = clazz;
        this.__dynamicProperties__ = new ArrayMemory();
        this.__env__ = env;
    }

    @Signature({
            @Arg(value = "message", optional = @Optional(value = "", type = HintType.STRING)),
            @Arg(value = "code", optional = @Optional(value = "0", type = HintType.INT)),
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
        this.init = false;
    }

    @Signature
    final public Memory getMessage(Environment env, Memory... args){
        return getProperties().valueOfIndex("message");
    }

    @Signature
    final public Memory getCode(Environment env, Memory... args){
        return getProperties().valueOfIndex("code");
    }

    @Signature
    final public Memory getLine(Environment env, Memory... args){
        return getProperties().valueOfIndex("line");
    }

    @Signature
    final public Memory getPosition(Environment env, Memory... args){
        return getProperties().valueOfIndex("position");
    }

    @Signature
    final public Memory getFile(Environment env, Memory... args){
        return getProperties().valueOfIndex("file");
    }

    @Signature
    final public Memory getTrace(Environment env, Memory... args){
        return getProperties().valueOfIndex("trace");
    }

    @Override
    public ClassEntity getReflection() {
        return __class__;
    }

    @Override
    public ArrayMemory getProperties() {
        if (!init){
            init = true;
            if (trace != null){
                __dynamicProperties__.refOfIndex("file").assign(trace.getFileName());
                __dynamicProperties__.refOfIndex("line").assign(trace.getStartLine() + 1);
                __dynamicProperties__.refOfIndex("position").assign(trace.getStartPosition() + 1);

                ArrayMemory backTrace = new ArrayMemory();
                for(CallStackItem el : callStack)
                    backTrace.add(el.toArray());

                __dynamicProperties__.refOfIndex("trace").assign(backTrace);
            }
        }

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
    @Override
    public Throwable fillInStackTrace() {
        return null;
    }

    @Override
    public Environment getEnvironment() {
        return __env__;
    }
}
