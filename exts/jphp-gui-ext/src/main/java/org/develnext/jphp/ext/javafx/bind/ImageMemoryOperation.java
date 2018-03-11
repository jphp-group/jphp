package org.develnext.jphp.ext.javafx.bind;

import javafx.scene.image.Image;
import org.develnext.jphp.ext.image.classes.PImage;
import org.develnext.jphp.ext.javafx.classes.UXImage;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.ext.core.classes.stream.Stream;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.support.MemoryOperation;

import java.io.InputStream;

public class ImageMemoryOperation extends MemoryOperation<Image> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class[] { Image.class };
    }

    @Override
    public Image convert(Environment environment, TraceInfo traceInfo, Memory memory) throws Throwable {
        if (memory.isNull()) return null;

        if (memory.instanceOf(UXImage.class)) {
            return memory.toObject(UXImage.class).getWrappedObject();
        }

        if (memory.instanceOf(PImage.class)) {
            return UXImage.ofNative(environment, memory).getWrappedObject();
        }

        InputStream inputStream = Stream.getInputStream(environment, memory);
        try {
            return new Image(inputStream);
        } finally {
            Stream.closeStream(environment, inputStream);
        }
    }

    @Override
    public Memory unconvert(Environment environment, TraceInfo traceInfo, Image image) throws Throwable {
        return new ObjectMemory(new UXImage(environment, image));
    }
}
