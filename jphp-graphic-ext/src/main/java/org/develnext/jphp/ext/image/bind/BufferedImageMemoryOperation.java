package org.develnext.jphp.ext.image.bind;

import org.develnext.jphp.ext.image.classes.PImage;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.support.MemoryOperation;
import php.runtime.reflection.ParameterEntity;
import php.runtime.reflection.support.TypeChecker;

import java.awt.image.BufferedImage;

public class BufferedImageMemoryOperation extends MemoryOperation<BufferedImage> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class[] { BufferedImage.class };
    }

    @Override
    public BufferedImage convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        return arg.isNull() ? null : arg.toObject(PImage.class).getImage();
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, BufferedImage arg) throws Throwable {
        return arg == null ? null : ObjectMemory.valueOf(new PImage(env, arg));
    }

    @Override
    public void applyTypeHinting(ParameterEntity parameter) {
        parameter.setTypeChecker(TypeChecker.of(PImage.class));
    }
}
