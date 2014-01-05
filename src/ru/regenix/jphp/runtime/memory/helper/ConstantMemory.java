package ru.regenix.jphp.runtime.memory.helper;

import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.env.TraceInfo;
import ru.regenix.jphp.runtime.memory.ReferenceMemory;
import ru.regenix.jphp.runtime.memory.support.Memory;

public class ConstantMemory extends ReferenceMemory {
    private final String name;
    private final String lowerName;

    public ConstantMemory(String name){
        super();
        this.name = name;
        this.lowerName = name.toLowerCase();
    }

    @Override
    public Memory toImmutable(Environment env, TraceInfo trace) {
        return env.__getConstant(name, lowerName, trace);
    }
}
