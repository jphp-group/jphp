package org.develnext.jphp.ext.compress.classes;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.develnext.jphp.ext.compress.CompressExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.stream.MiscStream;
import php.runtime.ext.core.classes.stream.Stream;
import php.runtime.ext.core.classes.stream.WrapIOException;
import php.runtime.memory.BinaryMemory;
import php.runtime.memory.LongMemory;
import php.runtime.reflection.ClassEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

@Name("Bzip2InputStream")
@Namespace(CompressExtension.NS)
public class PBZip2InputStream extends MiscStream {
    public PBZip2InputStream(Environment env, BZip2CompressorInputStream outputStream) {
        super(env, outputStream);
    }

    public PBZip2InputStream(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct(Environment env, InputStream inputStream) throws IOException {
        __construct(env, inputStream, false);
    }

    @Signature
    public void __construct(Environment env, InputStream inputStream, boolean decompressConcatenated)
            throws IOException {
            this.inputStream = new BZip2CompressorInputStream(inputStream, decompressConcatenated);
    }
}
