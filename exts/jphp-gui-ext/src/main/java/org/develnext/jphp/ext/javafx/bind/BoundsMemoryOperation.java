package org.develnext.jphp.ext.javafx.bind;

import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.support.MemoryOperation;
import php.runtime.reflection.ParameterEntity;
import php.runtime.reflection.support.TypeChecker;

public class BoundsMemoryOperation extends MemoryOperation<Bounds> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { Bounds.class };
    }

    @Override
    public Bounds convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        throw new IllegalStateException("Unable to convert memory to Bounds");
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, Bounds arg) throws Throwable {
        if (arg == null) {
            return Memory.NULL;
        }

        ArrayMemory r = new ArrayMemory();
        r.refOfIndex("width").assign(arg.getWidth());
        r.refOfIndex("height").assign(arg.getHeight());
        r.refOfIndex("depth").assign(arg.getDepth());

        r.refOfIndex("x").assign(arg.getMinX());
        r.refOfIndex("y").assign(arg.getMinY());
        r.refOfIndex("z").assign(arg.getMinZ());

        r.refOfIndex("maxX").assign(arg.getMaxX());
        r.refOfIndex("maxY").assign(arg.getMaxY());
        r.refOfIndex("maxZ").assign(arg.getMaxZ());

        return r.toConstant();
    }
}
