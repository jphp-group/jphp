package php.runtime.ext.core.stream;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.memory.BinaryMemory;
import php.runtime.memory.LongMemory;
import php.runtime.reflection.ClassEntity;

import java.io.*;
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
        exception(env, "Cannot read stream");
    }

    @Override
    @Signature({@Arg("path"), @Arg(value = "mode", optional = @Optional("r"))})
    public Memory __construct(Environment env, Memory... args) {
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
            exception(env, "Unknown type stream - %s", path);

        return Memory.NULL;
    }


    @Signature({@Arg("value"), @Arg(value = "length", optional = @Optional("NULL"))})
    public Memory write(Environment env, Memory... args) {
        int len = args[1].toInteger();
        byte[] bytes = args[0].getBinaryBytes();

        try {
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
                exception(env, "Cannot write to input stream");
                return Memory.CONST_INT_0;
            }

            return Memory.CONST_INT_0;
        } catch (IOException e) {
            exception(env, e.getMessage());
        }

        return Memory.FALSE;
    }

    @Signature(@Arg("length"))
    public Memory read(Environment env, Memory... args){
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
            try {
                int read;
                read = inputStream.read(buf);
                eof = read == -1;
                if (read == -1)
                    return Memory.FALSE;

                position += read;
                return new BinaryMemory(Arrays.copyOf(buf, read));
            } catch (IOException e) {
                exception(env, e.getMessage());
            }
        } else
            exception(env, "Cannot read from output stream");

        return Memory.NULL;
    }

    @Signature
    public Memory readFully(Environment env, Memory... args){
        if (memoryStream != null){
            byte[] result = memoryStream.readFully();
            if (result != null)
                return new BinaryMemory(result);
            else
                return Memory.FALSE;
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
    public Memory close(Environment env, Memory... args){
        try {
            if (memoryStream != null){
              memoryStream.close();
            } if (inputStream != null)
                inputStream.close();
            else if (outputStream != null)
                outputStream.close();
        } catch (IOException e) {
            exception(env, e.getMessage());
        }
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
                exception(env, "Cannot seek to %s", args[0].toInteger());

            this.position = args[0].toInteger();
        } else {
            exception(env, "Cannot seek in input/output stream");
        }
        return Memory.NULL;
    }

    @Signature
    public Memory length(Environment env, Memory... args){
        if (memoryStream != null)
            return LongMemory.valueOf(memoryStream.length);
        else
            exception(env, "Unsupported method for this type stream");
        return Memory.NULL;
    }

    @Signature
    public Memory flush(Environment env, Memory... args){
        if (outputStream == null)
            exception(env, "Only output stream support flush()");

        try {
            outputStream.flush();
        } catch (IOException e) {
            exception(env, e.getMessage());
        }
        return Memory.NULL;
    }
}
