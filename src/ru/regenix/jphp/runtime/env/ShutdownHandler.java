package ru.regenix.jphp.runtime.env;

import ru.regenix.jphp.runtime.invoke.Invoker;
import ru.regenix.jphp.runtime.memory.support.Memory;

public class ShutdownHandler {
    public final Invoker invoker;
    public final Memory[] args;

    public ShutdownHandler(Invoker invoker, Memory[] args) {
        this.invoker = invoker;
        this.args = args;
    }

    public void call() throws Throwable {
        invoker.call(args);
    }
}
