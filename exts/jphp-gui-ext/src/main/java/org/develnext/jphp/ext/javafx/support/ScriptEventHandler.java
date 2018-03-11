package org.develnext.jphp.ext.javafx.support;

import javafx.event.Event;
import javafx.event.EventHandler;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.invoke.Invoker;
import php.runtime.memory.support.MemoryOperation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ScriptEventHandler<T extends Event> implements EventHandler<T> {
    protected final Environment env;
    protected final Map<String, Invoker> invokerMap;

    public ScriptEventHandler(Environment env) {
        this.env = env;
        this.invokerMap = new ConcurrentHashMap<String, Invoker>();
    }

    public void set(Invoker invoker, String group) {
        invokerMap.put(group, invoker);
    }

    public void set(Invoker invoker) {
        set(invoker, "general");
    }

    public void unset(String group) {
        invokerMap.remove(group);
    }

    public void clear() {
        invokerMap.clear();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void handle(T t) {
        Memory event = Memory.NULL;

        if (t != null) {
            MemoryOperation operation = MemoryOperation.get(t.getClass(), null);

            if (operation != null) {
                try {
                    event = operation.unconvert(env, TraceInfo.UNKNOWN, t);
                } catch (Throwable throwable) {
                    env.wrapThrow(throwable);
                }
            }
        }

        for (Invoker invoker : invokerMap.values()) {
            try {
                invoker.call(event);
            } catch (Throwable throwable) {
                env.wrapThrow(throwable);
            }
        }
    }
}
