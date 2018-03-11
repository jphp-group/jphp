package org.develnext.jphp.ext.javafx.bind;

import javafx.scene.paint.Color;
import org.develnext.jphp.ext.image.classes.PColor;
import org.develnext.jphp.ext.javafx.classes.paint.UXColor;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.support.MemoryOperation;

public class ColorMemoryOperation extends MemoryOperation<Color> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { Color.class };
    }

    @Override
    public Color convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        if (arg.instanceOf(UXColor.class)) {
            return arg.toObject(UXColor.class).getWrappedObject();
        }

        if (arg.instanceOf(PColor.class)) {
            java.awt.Color c = arg.toObject(PColor.class).getColor();
            return Color.rgb(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
        }

        try {
            return Color.valueOf(arg.toString());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, Color arg) throws Throwable {
        return arg == null ? Memory.NULL : new ObjectMemory(new UXColor(env, arg));
    }
}
