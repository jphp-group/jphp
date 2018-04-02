package org.develnext.jphp.ext.compress.classes;

import static php.runtime.common.HintType.ARRAY;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
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
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.stream.Stream;
import php.runtime.ext.core.classes.stream.WrapIOException;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.BinaryMemory;
import php.runtime.memory.LongMemory;
import php.runtime.reflection.ClassEntity;

@Name("GzipInputStream")
@Namespace(CompressExtension.NS)
public class PGzipInputStream extends Stream {
    private int position;
    private boolean eof;

    private GzipCompressorInputStream inputStream;

    public PGzipInputStream(Environment env, GzipCompressorInputStream outputStream) {
        super(env);
        this.inputStream = outputStream;
    }

    public PGzipInputStream(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct(Environment env, InputStream inputStream) throws IOException {
        __construct(env, inputStream, false);
    }

    @Signature
    public void __construct(Environment env, InputStream inputStream, boolean decompressConcatenated)
            throws IOException {
            this.inputStream = new GzipCompressorInputStream(inputStream, decompressConcatenated);
    }

    @Override
    @Signature
    public Memory write(Environment env, Memory... args) throws IOException {
        throw new IOException("Cannot write to input stream");
    }

    @Override
    public Memory read(Environment env, Memory... args) throws IOException {
        int len = args[0].toInteger();
        if (len <= 0)
            return Memory.FALSE;

        byte[] buf = new byte[len];
        int read;
        read = inputStream.read(buf);
        eof = read == -1;
        if (read == -1)
            return Memory.FALSE;

        position += read;
        return new BinaryMemory(Arrays.copyOf(buf, read));
    }

    @Override
    public Memory readFully(Environment env, Memory... args) throws IOException {
        byte[] buff = new byte[1024];
        int len;

        ByteArrayOutputStream tmp = new ByteArrayOutputStream();
        while ((len = inputStream.read(buff)) > 0) {
            tmp.write(buff, 0, len);
        }

        return new BinaryMemory(tmp.toByteArray());
    }

    @Override
    public Memory eof(Environment env, Memory... args) {
        return eof ? Memory.TRUE : Memory.FALSE;
    }

    @Override
    public Memory seek(Environment env, Memory... args) throws IOException {
        env.exception(WrapIOException.class, "Cannot seek in input stream");
        return Memory.NULL;
    }

    @Override
    public Memory getPosition(Environment env, Memory... args) {
        return Memory.NULL;
    }

    @Override
    public Memory close(Environment env, Memory... args) throws IOException {
        inputStream.close();
        return Memory.NULL;
    }
}
