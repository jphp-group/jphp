package ru.regenix.jphp.compiler.common.compile;

import ru.regenix.jphp.compiler.jvm.runtime.memory.Memory;
import ru.regenix.jphp.compiler.jvm.runtime.memory.MemoryUtils;

public class CompileConstant {
    public final String name;
    public final Memory value;

    public CompileConstant(String name, Memory value) {
        this.name = name;
        this.value = value;
    }

    public CompileConstant(String name, Object value){
        this(name, MemoryUtils.valueOf(value));
    }
}
