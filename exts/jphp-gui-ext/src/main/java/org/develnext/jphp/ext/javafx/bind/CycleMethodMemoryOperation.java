package org.develnext.jphp.ext.javafx.bind;

import javafx.scene.paint.CycleMethod;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.StringMemory;
import php.runtime.memory.support.MemoryOperation;

public class CycleMethodMemoryOperation extends MemoryOperation<CycleMethod>{
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class[]{CycleMethod.class};
    }

    @Override
    public CycleMethod convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        return CycleMethod.valueOf(arg.toString());
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, CycleMethod arg) throws Throwable {
        return StringMemory.valueOf(arg.toString());
    }
}
