package ru.regenix.jphp.runtime.invokedynamic;

import ru.regenix.jphp.runtime.memory.Memory;
import ru.regenix.jphp.runtime.env.Environment;

import java.lang.reflect.Method;

@SuppressWarnings({ "unused", "rawtypes" })
public class CallableValue {

    protected final Method method;

    public CallableValue(Method clazz) {
        this.method = clazz;
    }

    public Memory call(Object self, Environment env, Memory[] args){
        try {
            Memory memory = (Memory) method.invoke(self, args);
            return memory == null ? Memory.NULL : memory;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
