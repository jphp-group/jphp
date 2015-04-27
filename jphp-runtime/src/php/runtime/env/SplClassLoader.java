package php.runtime.env;

import php.runtime.Memory;
import php.runtime.invoke.Invoker;
import php.runtime.invoke.StaticMethodInvoker;
import php.runtime.memory.ArrayMemory;

public class SplClassLoader {
    protected Invoker invoker;
    protected Memory callback;

    public SplClassLoader(Invoker invoker, Memory callback){
        this.invoker = invoker;
        this.callback = callback;
        if (invoker instanceof StaticMethodInvoker && !callback.isArray()){
            StaticMethodInvoker staticMethodInvoker = (StaticMethodInvoker)invoker;
            this.callback = ArrayMemory.ofStrings(
                    staticMethodInvoker.getCalledClass(),
                    staticMethodInvoker.getMethod().getName()
            );
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SplClassLoader)) return false;

        SplClassLoader that = (SplClassLoader) o;

        return invoker.equals(that.invoker);
    }

    @Override
    public int hashCode() {
        int result = invoker.hashCode();
        return result;
    }

    public SplClassLoader forEnvironment(Environment newEnvironment) {
        Invoker invoker = this.invoker.forEnvironment(newEnvironment);
        return new SplClassLoader(invoker, callback);
    }

    public boolean load(Memory... args) {
        return invoker.callNoThrow(args).toBoolean();
    }

    public Invoker getInvoker() {
        return invoker;
    }

    public Memory getCallback() {
        return callback;
    }
}
