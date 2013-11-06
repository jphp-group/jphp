package ru.regenix.jphp.compiler.common.compile;

import ru.regenix.jphp.runtime.memory.Memory;
import ru.regenix.jphp.runtime.memory.MemoryUtils;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CompileConstant)) return false;

        CompileConstant that = (CompileConstant) o;

        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
