package org.develnext.jphp.ext.compress.classes;

import org.apache.commons.compress.compressors.lz4.BlockLZ4CompressorInputStream;
import org.apache.commons.compress.compressors.lz4.FramedLZ4CompressorInputStream;
import org.apache.commons.compress.compressors.lz77support.AbstractLZ77CompressorInputStream;
import org.develnext.jphp.ext.compress.CompressExtension;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.stream.MiscStream;
import php.runtime.reflection.ClassEntity;

import java.io.IOException;
import java.io.InputStream;

@Name("Lz4InputStream")
@Namespace(CompressExtension.NS)
public class PLz4InputStream extends MiscStream {
    public PLz4InputStream(Environment env, AbstractLZ77CompressorInputStream outputStream) {
        super(env, outputStream);
    }

    public PLz4InputStream(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct(Environment env, InputStream inputStream) throws IOException {
        __construct(env, inputStream, false);
    }

    @Signature
    public void __construct(Environment env, InputStream inputStream, boolean framed)
            throws IOException {
            this.inputStream = framed ? new FramedLZ4CompressorInputStream(inputStream) : new BlockLZ4CompressorInputStream(inputStream);
    }
}
