package php.runtime.invoke;

import php.runtime.Memory;
import php.runtime.common.Messages;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.exceptions.CriticalException;
import php.runtime.exceptions.support.ErrorType;
import php.runtime.ext.core.classes.WrapInvoker;
import php.runtime.lang.ForeachIterator;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.support.MemoryOperation;
import php.runtime.reflection.ParameterEntity;

abstract public class Invoker implements Cloneable {
    protected Environment env;
    protected TraceInfo trace;

    private Memory memory;
    private Object userData;

    protected Invoker(Environment env, TraceInfo trace) {
        this.env = env;
        this.trace = trace;
    }

    @Override
    protected Invoker clone() throws CloneNotSupportedException {
        return (Invoker) super.clone();
    }

    public Memory getMemory() {
        return memory;
    }

    public void setMemory(Memory memory) {
        this.memory = memory;
    }

    public Object getUserData() {
        return userData;
    }

    public void setUserData(Object userData) {
        this.userData = userData;
    }

    public Invoker forEnvironment(Environment env) {
        try {
            Invoker clone = clone();
            clone.env = env;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new CriticalException(e);
        }
    }

    public Environment getEnvironment() {
        return env;
    }

    public void check(String name, TraceInfo trace){

    }

    abstract public ParameterEntity[] getParameters();

    abstract public String getName();

    abstract public int getArgumentCount();

    abstract protected void pushCall(TraceInfo trace, Memory[] args);

    abstract protected Memory invoke(Memory... args) throws Throwable;

    final public Memory call(Memory... args) throws Throwable {
        trace = trace == null ? (env == null ? TraceInfo.UNKNOWN : env.trace()) : trace;
        /*pushCall(trace, args);
        try {*/
            return invoke(args);
        /*} finally {
            popCall();
        }*/
    }

    final public Memory callAny(Object... args) {
        if (args != null && args.length > 0) {
            Memory[] passed = new Memory[args.length];

            for (int i = 0; i < passed.length; i++) {
                if (args[i] == null) {
                    passed[i] = Memory.NULL;
                    continue;
                }

                MemoryOperation operation = MemoryOperation.get(
                        args[i].getClass(), args[i].getClass().getGenericSuperclass()
                );

                if (operation == null) {
                    throw new CriticalException("Unsupported bind type - " + args[i].getClass().toString());
                }

                passed[i] = operation.unconvertNoThow(env, trace, args[i]);
            }

            return callNoThrow(passed);
        } else {
            return callNoThrow();
        }
    }

    public Memory callNoThrow(Memory... args){
        try {
            return call(args);
        } catch (RuntimeException e){
            throw e;
        } catch (Throwable e){
            throw new RuntimeException(e);
        }
    }

    protected void popCall(){
        env.popCall();
    }

    public void setTrace(TraceInfo trace) {
        this.trace = trace;
    }

    /**
     * Use create() method.
     */
    @Deprecated
    public static Invoker valueOf(Environment env, Memory method){
        return valueOf(env, env.peekCall(0).trace, method);
    }

    abstract public int canAccess(Environment env);

    public static Invoker create(Environment env, Memory method) {
        Invoker invoker = Invoker.valueOf(env, null, method);

        if (invoker != null) {
            invoker.setMemory(method);
        }

        return invoker;
    }

    public static Invoker valueOf(Environment env, TraceInfo trace, Memory method){
        method = method.toValue();
        if (method.isObject()) {
            if (method.toValue(ObjectMemory.class).value instanceof WrapInvoker) {
                return method.toObject(WrapInvoker.class).getInvoker();
            }

            return DynamicMethodInvoker.valueOf(env, trace, method);
        } else if (method.isArray()){
            Memory one = null, two = null;
            ForeachIterator iterator = method.getNewIterator(env, false, false);
            while (iterator.next()){
                if (one == null)
                    one = iterator.getValue();
                else if (two == null)
                    two = iterator.getValue();
                else
                    break;
            }

            if (one == null || two == null) {
                if (trace == null) {
                    return null;
                }
                env.error(trace, ErrorType.E_ERROR, Messages.ERR_CALL_TO_UNDEFINED_FUNCTION.fetch(method.toString()));
            }

            assert one != null;
            assert two != null;
            String methodName = two.toString();
            if (one.isObject()) {
                return DynamicMethodInvoker.valueOf(env, trace, one.toValue(), methodName);
            } else {
                String className = one.toString();
                return StaticMethodInvoker.valueOf(env, trace, className, methodName);
            }
        } else {
            String methodName = method.toString();
            int p;
            if ((p = methodName.indexOf("::")) > -1) {
                String className = methodName.substring(0, p);
                methodName = methodName.substring(p + 2, methodName.length());
                return StaticMethodInvoker.valueOf(env, trace, className, methodName);
            } else {
                return FunctionInvoker.valueOf(env, trace, methodName);
            }
        }
    }
}
