package php.runtime.invoke;

import php.runtime.Memory;
import php.runtime.common.Callback;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.ext.core.classes.WrapInvoker;
import php.runtime.memory.ObjectMemory;
import php.runtime.reflection.ParameterEntity;

public class RunnableInvoker extends Invoker {
    private final Callback<Memory, Memory[]> runnable;

    private RunnableInvoker(Environment env, Callback<Memory, Memory[]> runnable) {
        super(env, env.trace());
        this.runnable = runnable;
    }

    @Override
    public ParameterEntity[] getParameters() {
        return new ParameterEntity[0];
    }

    @Override
    public String getName() {
        return "Closure";
    }

    @Override
    public int getArgumentCount() {
        return 0;
    }

    @Override
    protected void pushCall(TraceInfo trace, Memory[] args) {
    }

    @Override
    protected Memory invoke(Memory... args) throws Throwable {
        return runnable.call(args);
    }

    @Override
    public int canAccess(Environment env) {
        return 0;
    }

    public static Memory make(Environment env, Callback<Memory, Memory[]> runnable) {
        RunnableInvoker invoker = new RunnableInvoker(env, runnable);
        WrapInvoker wrapInvoker = new WrapInvoker(env, invoker);

        return ObjectMemory.valueOf(wrapInvoker);
    }
}
