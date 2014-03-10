package php.runtime.ext.core.stream;

import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.launcher.Launcher;
import php.runtime.memory.BinaryMemory;
import php.runtime.memory.LongMemory;
import php.runtime.reflection.ClassEntity;

import java.io.*;
import java.util.Arrays;

import static php.runtime.annotation.Reflection.Name;

@Name("php\\io\\ResourceStream")
public class ResourceStream extends Stream {
    protected InputStream stream;
    protected long position = 0;
    protected boolean eof = false;

    public ResourceStream(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    @Reflection.Signature({@Reflection.Arg("path")})
    public Memory __construct(Environment env, Memory... args) {
        Launcher launcher = Launcher.current();
        if (launcher == null)
            throw new IllegalArgumentException("Launcher is not running");

        stream = launcher.getResource(args[0].toString());
        if (stream == null)
            exception(env, "Resource not found - %s", args[0]);

        return Memory.NULL;
    }

    @Override
    public Memory write(Environment env, Memory... args) throws IOException {
        throw new IOException("Stream only for reading");
    }

    @Override
    public Memory read(Environment env, Memory... args) throws IOException {
        int len = args[0].toInteger();
        if (len <= 0)
            return Memory.FALSE;

        byte[] buf = new byte[len];
        int read;
        read = stream.read(buf);
        eof = read == -1;
        if (read == -1)
            return Memory.FALSE;

        position += read;
        return new BinaryMemory(Arrays.copyOf(buf, read));
    }

    @Override
    public Memory readFully(Environment env, Memory... args) throws IOException {
        ByteArrayOutputStream tmp = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int read;

        while ((read = stream.read(buf)) > 0) {
            tmp.write(buf, 0, read);
            position += read;
        }

        return new BinaryMemory(tmp.toByteArray());
    }

    @Override
    public Memory eof(Environment env, Memory... args) {
        return eof ? Memory.TRUE : Memory.FALSE;
    }

    @Override
    public Memory seek(Environment env, Memory... args) throws IOException {
        throw new IOException("Cannot seek");
    }

    @Override
    public Memory getPosition(Environment env, Memory... args) {
        return LongMemory.valueOf(position);
    }

    @Override
    public Memory close(Environment env, Memory... args) throws IOException {
        stream.close();
        return Memory.NULL;
    }
}
