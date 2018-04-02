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
public class PGzipOutputStream extends Stream {
    private int position;
    private boolean eof;
    private GzipCompressorOutputStream outputStream;

    public PGzipOutputStream(Environment env, GzipCompressorOutputStream outputStream) {
        super(env);
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

    @Override
    @Signature
    public Memory write(Environment env, Memory... args) throws IOException {
        int len = args[1].toInteger();
        byte[] bytes = args[0].getBinaryBytes(env.getDefaultCharset());
        len = len == 0 || len > bytes.length ? bytes.length : len;

        outputStream.write(bytes, 0, len);
        this.position += len;

        return LongMemory.valueOf(len);
    }

    @Override
    public Memory read(Environment env, Memory... args) throws IOException {
        throw new IOException("Cannot read from output stream");
    }

    @Override
    public Memory readFully(Environment env, Memory... args) throws IOException {
        throw new IOException("Cannot read from output stream");
    }

    @Override
    public Memory eof(Environment env, Memory... args) {
        return Memory.FALSE;
    }

    @Override
    public Memory seek(Environment env, Memory... args) throws IOException {
        env.exception(WrapIOException.class, "Cannot seek in output stream");
        return Memory.NULL;
    }

    @Override
    public Memory getPosition(Environment env, Memory... args) {
        return Memory.NULL;
    }

    @Override
    public Memory close(Environment env, Memory... args) throws IOException {
        outputStream.close();
        return Memory.NULL;
    }
}
