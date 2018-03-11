package org.develnext.jphp.ext.javafx.bind;

import javafx.geometry.Insets;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.support.MemoryOperation;

public class InsetsMemoryOperation extends MemoryOperation<Insets> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { Insets.class };
    }

    @Override
    public Insets convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        if (arg.isArray()) {
            ArrayMemory array = arg.toValue(ArrayMemory.class);
            Memory[] values = array.values();

            switch (values.length) {
                case 0:
                    return new Insets(0);
                case 1:
                    return new Insets(values[1].toDouble());
                case 2:
                    return new Insets(values[0].toDouble(), values[1].toDouble(), values[0].toDouble(), values[1].toDouble());
                case 3:
                    return new Insets(values[0].toDouble(), values[1].toDouble(), values[2].toDouble(), values[1].toDouble());
                default:
                    return new Insets(values[0].toDouble(), values[1].toDouble(), values[2].toDouble(), values[3].toDouble());
            }
        } else if (arg.isNull()) {
            return null;
        } else {
            return new Insets(arg.toDouble());
        }
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, Insets arg) throws Throwable {
        return arg == null
                ? Memory.NULL
                : ArrayMemory.ofDoubles(arg.getTop(), arg.getRight(), arg.getBottom(), arg.getLeft()).toConstant();
    }
}
