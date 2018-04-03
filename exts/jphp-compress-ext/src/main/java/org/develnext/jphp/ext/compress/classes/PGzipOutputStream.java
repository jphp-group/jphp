package org.develnext.jphp.ext.compress.classes;

import static php.runtime.common.HintType.ARRAY;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipParameters;
import org.develnext.jphp.ext.compress.CompressExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection.Arg;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Nullable;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.stream.MiscStream;
import php.runtime.ext.core.classes.stream.Stream;
import php.runtime.ext.core.classes.stream.WrapIOException;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.LongMemory;
import php.runtime.reflection.ClassEntity;

@Name("GzipOutputStream")
@Namespace(CompressExtension.NS)
public class PGzipOutputStream extends MiscStream {
    public PGzipOutputStream(Environment env, GzipCompressorOutputStream outputStream) {
        super(env, outputStream);
        this.outputStream = outputStream;
    }

    public PGzipOutputStream(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct(Environment env, OutputStream outputStream) throws IOException {
        __construct(env, outputStream, Memory.NULL);
    }

    @Signature
    public void __construct(Environment env, OutputStream outputStream, @Nullable @Arg(type = ARRAY) Memory parameters)
            throws IOException {
        if (parameters.isNull()) {
            this.outputStream = new GzipCompressorOutputStream(outputStream);
        } else {
            this.outputStream = new GzipCompressorOutputStream(
                    outputStream, parameters.toValue(ArrayMemory.class).toBean(env, GzipParameters.class)
            );
        }
    }
}
