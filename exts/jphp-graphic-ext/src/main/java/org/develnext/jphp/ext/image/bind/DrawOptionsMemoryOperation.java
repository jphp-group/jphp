package org.develnext.jphp.ext.image.bind;

import org.develnext.jphp.ext.image.support.DrawOptions;
import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.support.MemoryOperation;
import php.runtime.reflection.ParameterEntity;
import php.runtime.reflection.support.TypeChecker;

public class DrawOptionsMemoryOperation extends MemoryOperation<DrawOptions> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class[] { DrawOptions.class };
    }

    @Override
    public DrawOptions convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        return arg.isNull() ? new DrawOptions() : arg.toValue(ArrayMemory.class).toBean(env, trace, DrawOptions.class);
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, DrawOptions arg) throws Throwable {
        return ArrayMemory.ofNullableBean(env, arg);
    }

    @Override
    public void applyTypeHinting(ParameterEntity parameter) {
        parameter.setNullable(true);
        parameter.setTypeChecker(TypeChecker.of(HintType.ARRAY));
    }
}
