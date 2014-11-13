package php.runtime.memory.helper;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.ReferenceMemory;

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

    public String getName() {
        return name;
    }

    @Override
    public Memory toValue() {
        return this;
    }
}
