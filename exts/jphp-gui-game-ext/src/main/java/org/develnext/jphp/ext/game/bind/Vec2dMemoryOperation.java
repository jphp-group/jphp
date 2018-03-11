package org.develnext.jphp.ext.game.bind;

import org.develnext.jphp.ext.game.support.Vec2d;
import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.support.MemoryOperation;
import php.runtime.reflection.ParameterEntity;
import php.runtime.reflection.support.TypeChecker;

public class Vec2dMemoryOperation extends MemoryOperation<Vec2d> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { Vec2d.class };
    }

    @Override
    public Vec2d convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        if (arg.isNull()) {
            return null;
        }

        return new Vec2d(arg.valueOfIndex(0).toDouble(), arg.valueOfIndex(1).toDouble());
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, Vec2d arg) throws Throwable {
        if (arg == null) {
            return null;
        }

        return ArrayMemory.ofDoubles(arg.x, arg.y);
    }

    @Override
    public void applyTypeHinting(ParameterEntity parameter) {
        parameter.setTypeChecker(TypeChecker.of(HintType.ARRAY));
    }
}
