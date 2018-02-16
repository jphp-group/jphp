package org.develnext.jphp.ext.image.bind;

import org.develnext.jphp.ext.image.classes.PFont;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.support.MemoryOperation;
import php.runtime.reflection.ParameterEntity;
import php.runtime.reflection.support.TypeChecker;

import java.awt.*;

public class FontMemoryOperation extends MemoryOperation<Font> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class[] { Font.class };
    }

    @Override
    public Font convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        return arg.isNull() ? null : arg.toObject(PFont.class).getFont();
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, Font arg) throws Throwable {
        return arg == null ? Memory.NULL : new ObjectMemory(new PFont(env, arg));
    }

    @Override
    public void applyTypeHinting(ParameterEntity parameter) {
        parameter.setTypeChecker(TypeChecker.of(PFont.class));
    }
}
