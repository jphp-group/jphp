package php.runtime.env;

import php.runtime.Memory;
import php.runtime.invoke.Invoker;
import php.runtime.invoke.StaticMethodInvoker;
import php.runtime.memory.ArrayMemory;

import java.util.HashSet;
import java.util.Set;

public class SplClassLoader {
    protected Invoker invoker;
    protected Memory callback;
    protected Set<String> locks = new HashSet<String>();

    public SplClassLoader(Invoker invoker, Memory callback){
        this.invoker = invoker;
        this.callback = callback;
        if (invoker instanceof StaticMethodInvoker && !callback.isArray()){
            StaticMethodInvoker staticMethodInvoker = (StaticMethodInvoker)invoker;
            this.callback = new ArrayMemory(
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

        if (!invoker.equals(that.invoker)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = invoker.hashCode();
        return result;
    }

    public boolean load(Memory... args){
        // recursion
        String name = args[0].toString().toLowerCase();
        if (!locks.add(name))
            return false;

        invoker.pushCall(null, new Memory[0]);
        try {
            return invoker.call(args).toBoolean();
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        } finally {
            locks.remove(name);
            invoker.popCall();
        }
    }

    public Invoker getInvoker() {
        return invoker;
    }

    public Memory getCallback() {
        return callback;
    }
}
