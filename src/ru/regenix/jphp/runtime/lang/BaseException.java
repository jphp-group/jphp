package ru.regenix.jphp.runtime.lang;

import ru.regenix.jphp.common.HintType;
import ru.regenix.jphp.common.Modifier;
import ru.regenix.jphp.runtime.env.CallStackItem;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.env.TraceInfo;
import ru.regenix.jphp.runtime.memory.ArrayMemory;
import ru.regenix.jphp.runtime.memory.StringMemory;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.reflection.ClassEntity;

import java.lang.ref.WeakReference;

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
    protected final ArrayMemory props;
    protected ClassEntity clazz;
    protected final WeakReference<Environment> env;
    protected TraceInfo trace;
    protected CallStackItem[] callStack;

    private boolean init = true;
    private boolean isFinalized = false;

    public BaseException(Environment env, ClassEntity clazz) {
        this.clazz = clazz;
        this.props = new ArrayMemory();
        this.env = new WeakReference<Environment>(env);
    }

    @Signature({
            @Arg(value = "message", optional = @Optional(value = "", type = HintType.STRING)),
            @Arg(value = "code", optional = @Optional(value = "0", type = HintType.INT)),
            @Arg(value = "previous", optional = @Optional(value = "NULL"))
    })
    public Memory __construct(Environment env, Memory... args) {
        clazz.refOfProperty(props, "message").assign(args[0].toString());
        clazz.refOfProperty(props, "code").assign(args[1].toLong());
        clazz.refOfProperty(props, "previous").assign(args[2]);

        return Memory.NULL;
    }

    public void setTraceInfo(Environment env, TraceInfo trace){
        this.callStack = env.getCallStackSnapshot();
        this.trace = trace;
        this.init = false;
    }

    @Signature
    final public Memory getMessage(Environment env, Memory... args){
        return clazz.refOfProperty(getProperties(), "message").toValue();
    }

    @Signature
    final public Memory getCode(Environment env, Memory... args){
        return clazz.refOfProperty(getProperties(), "code").toValue();
    }

    @Signature
    final public Memory getLine(Environment env, Memory... args){
        return clazz.refOfProperty(getProperties(), "line").toValue();
    }

    @Signature
    final public Memory getPosition(Environment env, Memory... args){
        return clazz.refOfProperty(getProperties(), "position").toValue();
    }

    @Signature
    final public Memory getFile(Environment env, Memory... args){
        return clazz.refOfProperty(getProperties(), "file").toValue();
    }

    @Signature
    final public Memory getTrace(Environment env, Memory... args){
        return clazz.refOfProperty(getProperties(), "trace").toValue();
    }

    @Signature
    final public Memory getTraceAsString(Environment env, Memory... args){
        int i = 0;
        StringBuilder sb = new StringBuilder();
        for (CallStackItem e : getCallStack()){
            if (i != 0)
                sb.append("\n");

            sb.append("#").append(i).append(" ").append(e.toString(false));
            i++;
        }
        if (i != 0)
            sb.append("\n");

        sb.append("#").append(i).append(" {main}");
        return new StringMemory(sb.toString());
    }

    @Override
    public ClassEntity getReflection() {
        return clazz;
    }

    @Override
    public ArrayMemory getProperties() {
        if (!init){
            init = true;
            if (trace != null){
                clazz.refOfProperty(props, "file").assign(trace.getFileName());
                clazz.refOfProperty(props, "line").assign(trace.getStartLine() + 1);
                clazz.refOfProperty(props, "position").assign(trace.getStartPosition() + 1);

                ArrayMemory backTrace = new ArrayMemory();
                for(CallStackItem el : callStack)
                    backTrace.add(el.toArray());

                clazz.refOfProperty(props, "trace").assign(backTrace);
            }
        }

        return props;
    }

    @Override
    final public int getPointer() {
        return super.hashCode();
    }

    @Override
    public boolean isMock() {
        return clazz == null;
    }

    @Override
    public void setAsMock() {
        clazz = null;
    }

    public CallStackItem[] getCallStack() {
        return callStack;
    }

    public TraceInfo getTrace() {
        return trace;
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
        return env.get();
    }

    @Override
    public boolean isFinalized() {
        return isFinalized;
    }

    @Override
    public void doFinalize() {
        isFinalized = true;
    }
}
