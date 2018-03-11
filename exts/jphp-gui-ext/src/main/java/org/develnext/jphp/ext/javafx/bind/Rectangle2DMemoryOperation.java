package org.develnext.jphp.ext.javafx.bind;

import javafx.geometry.Rectangle2D;
import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.support.MemoryOperation;
import php.runtime.reflection.ParameterEntity;
import php.runtime.reflection.support.TypeChecker;

public class Rectangle2DMemoryOperation extends MemoryOperation<Rectangle2D> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { Rectangle2D.class };
    }

    @Override
    public Rectangle2D convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        if (arg.isNull()) {
            return null;
        }

        return new Rectangle2D(
                arg.valueOfIndex("x").toDouble(),
                arg.valueOfIndex("y").toDouble(),
                arg.valueOfIndex("width").toDouble(),
                arg.valueOfIndex("height").toDouble()
        );
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, Rectangle2D arg) throws Throwable {
        ArrayMemory r = new ArrayMemory();
        r.refOfIndex("width").assign(arg.getWidth());
        r.refOfIndex("height").assign(arg.getHeight());

        r.refOfIndex("x").assign(arg.getMinX());
        r.refOfIndex("y").assign(arg.getMinY());

        return r.toConstant();
    }

    @Override
    public void applyTypeHinting(ParameterEntity parameter) {
        parameter.setTypeChecker(new TypeChecker.Simple(HintType.ARRAY));
    }
}
