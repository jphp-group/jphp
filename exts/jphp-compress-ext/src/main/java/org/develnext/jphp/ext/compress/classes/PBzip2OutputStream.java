package org.develnext.jphp.ext.compress.classes;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipParameters;
import org.develnext.jphp.ext.compress.CompressExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.stream.MiscStream;
import php.runtime.ext.core.classes.stream.Stream;
import php.runtime.ext.core.classes.stream.WrapIOException;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.LongMemory;
import php.runtime.reflection.ClassEntity;

import java.io.IOException;
import java.io.OutputStream;

import static php.runtime.common.HintType.ARRAY;

@Name("Bzip2OutputStream")
@Namespace(CompressExtension.NS)
public class PBzip2OutputStream extends MiscStream {
    public PBzip2OutputStream(Environment env, BZip2CompressorOutputStream outputStream) {
        super(env, outputStream);
    }

    public PBzip2OutputStream(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct(Environment env, OutputStream outputStream) throws IOException {
        __construct(env, outputStream, 9);
    }

    @Signature
    public void __construct(Environment env, OutputStream outputStream, int blockSize)
            throws IOException {
        this.outputStream = new BZip2CompressorOutputStream(outputStream, blockSize);
    }
}
