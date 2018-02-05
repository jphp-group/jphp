package php.runtime.ext.core.classes.stream;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.memory.BinaryMemory;
import php.runtime.memory.LongMemory;
import php.runtime.reflection.ClassEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import static php.runtime.annotation.Reflection.*;

@Name("php\\io\\MiscStream")
public class MiscStream extends Stream {
    protected boolean canRead = true;
    protected int position = 0;

    protected boolean eof = true;
    protected MemoryStream memoryStream;
    protected InputStream inputStream;
    protected OutputStream outputStream;

    public MiscStream(Environment env, MemoryStream memoryStream) {
        super(env);
        this.memoryStream = memoryStream;
    }

    public MiscStream(Environment env, InputStream inputStream) {
        super(env);
        this.inputStream = inputStream;
    }

    public MiscStream(Environment env, OutputStream outputStream) {
        super(env);
        this.outputStream = outputStream;
    }

    public MiscStream(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    private void throwCannotRead(Environment env){
        env.exception(WrapIOException.class, "Cannot read stream");
    }

    @Override
    @Signature({@Arg("path"), @Arg(value = "mode", optional = @Optional("r"))})
    public Memory __construct(Environment env, Memory... args) throws IOException {
        super.__construct(env, args);

        String path = getPath();
        if ("memory".equals(path)){
            memoryStream = new MemoryStream();
        } else if ("stdout".endsWith(path)){
            outputStream = System.out;
        } else if ("stdin".equals(path)) {
            inputStream = System.in;
        } else if ("stderr".equals(path)){
            outputStream = System.err;
        } else
            env.exception(WrapIOException.class, "Unknown type stream - %s", path);

        return Memory.NULL;
    }


    @Signature({@Arg("value"), @Arg(value = "length", optional = @Optional("NULL"))})
    public Memory write(Environment env, Memory... args) throws IOException {
        int len = args[1].toInteger();
        byte[] bytes = args[0].getBinaryBytes(env.getDefaultCharset());

        eof = false;
        len = len == 0 || len > bytes.length ? bytes.length : len;
        if (memoryStream != null){
            memoryStream.write(bytes, 0, len);
            return LongMemory.valueOf(len);
        } else if (outputStream != null) {
            outputStream.write(bytes, 0, len);
            this.position += len;
            inputStream = null;
            return LongMemory.valueOf(len);
        } else if (inputStream != null){
            env.exception(WrapIOException.class, "Cannot write to input stream");
            return Memory.CONST_INT_0;
        }

        return Memory.CONST_INT_0;
    }

    @Signature(@Arg("length"))
    public Memory read(Environment env, Memory... args) throws IOException {
        if (!canRead)
            throwCannotRead(env);

        int len = args[0].toInteger();
        if (len <= 0)
            return Memory.FALSE;

        if (memoryStream != null){
            byte[] result = memoryStream.read(len);
            if (result == null)
                return Memory.FALSE;

            return new BinaryMemory(result);
        } else if (inputStream != null){
            byte[] buf = new byte[len];
            int read;
            read = inputStream.read(buf);
            eof = read == -1;
            if (read == -1)
                return Memory.FALSE;

            position += read;
            return new BinaryMemory(Arrays.copyOf(buf, read));
        } else
            throw new IOException("Cannot read from output stream");
    }

    @Signature
    public Memory readFully(Environment env, Memory... args) throws IOException {
        if (memoryStream != null){
            byte[] result = memoryStream.readFully();
            if (result != null) {
                return new BinaryMemory(result);
            } else {
                return Memory.FALSE;
            }
        } else if (inputStream != null) {
            byte[] buff = new byte[1024];
            int len;

            ByteArrayOutputStream tmp = new ByteArrayOutputStream();
            while ((len = inputStream.read(buff)) > 0) {
                tmp.write(buff, 0, len);
            }

            return new BinaryMemory(tmp.toByteArray());
        }
        return Memory.NULL;
    }

    @Signature
    public Memory eof(Environment env, Memory... args){
        if (memoryStream != null)
            return memoryStream.eof() ? Memory.TRUE : Memory.FALSE;

        return eof ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory close(Environment env, Memory... args) throws IOException {
        if (memoryStream != null){
          memoryStream.close();
        } if (inputStream != null)
            inputStream.close();
        else if (outputStream != null)
            outputStream.close();
        return Memory.NULL;
    }

    @Signature
    public Memory getPosition(Environment env, Memory... args){
        if (memoryStream != null)
            return LongMemory.valueOf(position);
        return LongMemory.valueOf(position);
    }

    @Signature(@Arg("position"))
    public Memory seek(Environment env, Memory... args){
        if (memoryStream != null){
            if (!memoryStream.seek(args[0].toInteger()))
                env.exception(WrapIOException.class, "Cannot seek to %s", args[0].toInteger());

            this.position = args[0].toInteger();
        } else {
            env.exception(WrapIOException.class, "Cannot seek in input/output stream");
        }
        return Memory.NULL;
    }

    @Signature
    public Memory length(Environment env, Memory... args){
        if (memoryStream != null)
            return LongMemory.valueOf(memoryStream.length);
        else
            env.exception(WrapIOException.class, "Unsupported method for this type stream");
        return Memory.NULL;
    }

    @Signature
    public Memory flush(Environment env, Memory... args) throws IOException {
        if (outputStream == null)
            env.exception(WrapIOException.class, "Only output stream support flush()");
        else
            outputStream.flush();
        return Memory.NULL;
    }
}
