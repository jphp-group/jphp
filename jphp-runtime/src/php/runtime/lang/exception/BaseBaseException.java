package php.runtime.lang.exception;

import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.BaseType;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.common.HintType;
import php.runtime.common.Modifier;
import php.runtime.env.CallStackItem;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.exceptions.JPHPException;
import php.runtime.lang.BaseException;
import php.runtime.lang.IObject;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

import java.lang.ref.WeakReference;

abstract public class BaseBaseException extends RuntimeException implements IObject, JPHPException {
    protected final ArrayMemory props;
    protected ClassEntity clazz;
    protected Environment env;
    protected TraceInfo trace;
    protected CallStackItem[] callStack;

    private boolean init = true;
    private boolean isFinalized = false;

    private String nativeMessage = null;

    public BaseBaseException(String message) {
        this((Environment) null);
        nativeMessage = message;
    }

    public BaseBaseException(Environment env){
        this(env, null);
        clazz = env == null ? null : env.fetchClass(getClass());
    }

    public BaseBaseException(Environment env, ClassEntity clazz) {
        this.clazz = clazz;
        this.props = new ArrayMemory(true);
        this.env = env;
    }

    public void setTraceInfo(Environment env, TraceInfo trace) {
        this.callStack = env.getCallStackSnapshot();
        this.trace = trace;
        this.init = false;
    }

    @Override
    public ClassEntity getReflection() {
        return clazz;
    }

    @Override
    public ArrayMemory getProperties() {
        if (!init){
            init = true;
            if (trace != null) {
                Memory m;
                m = clazz.refOfProperty(props, "file");
                if (m.isNull()) {
                    m.assign(trace.getFileName());
                }

                m = clazz.refOfProperty(props, "line");
                if (m.isNull()) {
                    m.assign(trace.getStartLine() + 1);
                }

                m = clazz.refOfProperty(props, "position");

                if (m.isNull()) {
                    m.assign(trace.getStartPosition() + 1);
                }

                ArrayMemory backTrace = new ArrayMemory();

                for(CallStackItem el : callStack) {
                    backTrace.add(el.toArray());
                }

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
        Environment env = getEnvironment();

        if (env != null) {
            env.applySourceMap(callStack);
        }

        return callStack;
    }

    public TraceInfo getTrace() {
        Environment env = getEnvironment();
        if (env != null) {
            return env.getTraceAppliedSourceMap(trace);
        }

        return trace == null ? TraceInfo.UNKNOWN : trace;
    }

    @Signature
    public Memory getMessage(Environment env, Memory... args) {
        if (nativeMessage != null) {
            return StringMemory.valueOf(nativeMessage);
        }

        return clazz.refOfProperty(getProperties(), "message").toValue();
    }

    @Signature
    public Memory getCode(Environment env, Memory... args){
        Memory code = clazz.refOfProperty(getProperties(), "code").toValue();
        return code.isNull() ? Memory.CONST_INT_0 : code;
    }

    @Signature
    public Memory getLine(Environment env, Memory... args){
        return clazz.refOfProperty(getProperties(), "line").toValue();
    }

    @Signature
    public Memory getPosition(Environment env, Memory... args){
        return clazz.refOfProperty(getProperties(), "position").toValue();
    }

    @Signature
    public Memory getFile(Environment env, Memory... args){
        return clazz.refOfProperty(getProperties(), "file").toValue();
    }

    @Signature
    public Memory getTrace(Environment env, Memory... args){
        return clazz.refOfProperty(getProperties(), "trace").toValue();
    }

    @Signature
    public Memory getPrevious(Environment env, Memory... args) {
        return clazz.refOfProperty(getProperties(), "previous").toValue();
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
    public Memory getTraceAsString(Environment env, Memory... args){
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
        return env;
    }

    @Override
    public boolean isFinalized() {
        return isFinalized;
    }

    @Override
    public void doFinalize() {
        isFinalized = true;
    }

    @Override
    public String toString() {
        if (clazz.methodMagicToString != null) {
            Environment environment = getEnvironment();

            if (environment != null) {
                return environment.invokeMethodNoThrow(this, "__toString").toString();
            }
        }

        return super.toString();
    }
}
