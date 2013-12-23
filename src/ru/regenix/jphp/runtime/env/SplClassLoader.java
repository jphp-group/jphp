package ru.regenix.jphp.runtime.env;

import ru.regenix.jphp.runtime.invoke.Invoker;
import ru.regenix.jphp.runtime.invoke.StaticMethodInvoker;
import ru.regenix.jphp.runtime.memory.ArrayMemory;
import ru.regenix.jphp.runtime.memory.StringMemory;
import ru.regenix.jphp.runtime.memory.support.Memory;

import java.lang.reflect.InvocationTargetException;

public class SplClassLoader {
    protected Invoker invoker;
    protected Memory callback;

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

    public boolean load(StringMemory className){
        invoker.pushCall(null, new Memory[0]);
        try {
            return invoker.call(className).toBoolean();
        } catch (InvocationTargetException e){
            throw new RuntimeException(e.getCause());
        } catch (IllegalAccessException e){
            throw new RuntimeException(e);
        } finally {
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
