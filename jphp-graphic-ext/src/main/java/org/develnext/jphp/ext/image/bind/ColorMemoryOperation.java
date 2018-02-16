package org.develnext.jphp.ext.image.bind;

import org.develnext.jphp.ext.image.classes.PColor;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.support.MemoryOperation;
import php.runtime.reflection.ParameterEntity;
import php.runtime.reflection.support.TypeChecker;

import java.awt.*;

public class ColorMemoryOperation extends MemoryOperation<Color> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class[] { Color.class };
    }

    @Override
    public Color convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        return arg.isNull() ? null : (arg.instanceOf(PColor.class) ? arg.toObject(PColor.class).getColor() : Color.decode(arg.toString()));
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, Color arg) throws Throwable {
        return arg == null ? Memory.NULL : new ObjectMemory(new PColor(env, arg));
    }
}
