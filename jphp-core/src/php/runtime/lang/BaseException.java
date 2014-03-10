package php.runtime.lang;

import php.runtime.common.HintType;
import php.runtime.common.Modifier;
import php.runtime.env.CallStackItem;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.exceptions.JPHPException;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.StringMemory;
import php.runtime.Memory;
import php.runtime.reflection.ClassEntity;

import java.lang.ref.WeakReference;

import static php.runtime.annotation.Reflection.*;


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
public class BaseException extends RuntimeException implements IObject, JPHPException {
    protected final ArrayMemory props;
    protected ClassEntity clazz;
    protected final WeakReference<Environment> env;
    protected TraceInfo trace;
    protected CallStackItem[] callStack;

    private boolean init = true;
    private boolean isFinalized = false;

    public BaseException(Environment env){
        this(env, null);
        clazz = env.fetchClass(getClass());
    }

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
        if (args.length > 1)
            clazz.refOfProperty(props, "code").assign(args[1].toLong());

        if (args.length > 2)
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
    public Memory __toString(Environment env, Memory... args){
        StringBuilder sb = new StringBuilder();
        sb.append("exception '")
                .append(clazz.getName()).append("' with message '")
                .append(clazz.refOfProperty(getProperties(), "message"))
                .append("' in ")
                .append(clazz.refOfProperty(getProperties(), "file"))
                .append(":").append(clazz.refOfProperty(getProperties(), "line"));
        sb.append("\nStack Trace:\n");
        sb.append(getTraceAsString(env));
        return new StringMemory(sb.toString());
    }

    @Signature
    final public Memory getTraceAsString(Environment env, Memory... args){
        int i = 0;
        StringBuilder sb = new StringBuilder();
        if (callStack != null){
            for (CallStackItem e : getCallStack()){
                if (i != 0)
                    sb.append("\n");

                sb.append("#").append(i).append(" ").append(e.toString(false));
                i++;
            }
            if (i != 0)
                sb.append("\n");

            sb.append("#").append(i).append(" {main}");
        }
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
                Memory m;
                m = clazz.refOfProperty(props, "file");
                if (m.isNull())
                    m.assign(trace.getFileName());

                m = clazz.refOfProperty(props, "line");
                if (m.isNull())
                    m.assign(trace.getStartLine() + 1);

                m = clazz.refOfProperty(props, "position");
                if (m.isNull())
                    m.assign(trace.getStartPosition() + 1);

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
