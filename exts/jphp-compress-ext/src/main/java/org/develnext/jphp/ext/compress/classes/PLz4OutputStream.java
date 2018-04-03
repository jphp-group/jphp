package org.develnext.jphp.ext.compress.classes;

import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.lz4.BlockLZ4CompressorOutputStream;
import org.apache.commons.compress.compressors.lz4.FramedLZ4CompressorOutputStream;
import org.develnext.jphp.ext.compress.CompressExtension;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.stream.MiscStream;
import php.runtime.reflection.ClassEntity;

import java.io.IOException;
import java.io.OutputStream;

@Name("Lz4OutputStream")
@Namespace(CompressExtension.NS)
public class PLz4OutputStream extends MiscStream {
    public PLz4OutputStream(Environment env, CompressorOutputStream outputStream) {
        super(env, outputStream);
        this.outputStream = outputStream;
    }

    public PLz4OutputStream(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct(Environment env, OutputStream outputStream) throws IOException {
        __construct(env, outputStream, false);
    }

    @Signature
    public void __construct(Environment env, OutputStream outputStream, boolean framed)
            throws IOException {
        this.outputStream = framed ? new FramedLZ4CompressorOutputStream(outputStream) : new BlockLZ4CompressorOutputStream(outputStream);
    }
}
