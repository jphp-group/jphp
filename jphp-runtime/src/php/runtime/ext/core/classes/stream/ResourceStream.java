package php.runtime.ext.core.classes.stream;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.BinaryMemory;
import php.runtime.memory.LongMemory;
import php.runtime.reflection.ClassEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;

import static php.runtime.annotation.Reflection.*;

@Name("php\\io\\ResourceStream")
public class ResourceStream extends Stream {
    protected InputStream stream;
    protected long position = 0;
    protected boolean eof = false;

    protected URL url;

    public ResourceStream(Environment env, InputStream stream) {
        super(env);
        this.stream = stream;
    }

    public ResourceStream(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public URL getUrl() {
        return url;
    }

    @Override
    @Signature({@Arg("path")})
    public Memory __construct(Environment env, Memory... args) throws IOException {
        super.__construct(env, args);
        setPath("res://" + this.getPath());

        url = Thread.currentThread().getContextClassLoader().getResource(args[0].toString());
        if (url == null)
            throw new IOException("Resource not found - " + args[0]);

        stream = url.openStream();
        return Memory.NULL;
    }

    @Override
    @Signature
    public Memory write(Environment env, Memory... args) throws IOException {
        throw new IOException("Stream only for reading");
    }

    @Override
    @Signature
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
    @Signature
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
    @Signature
    public Memory eof(Environment env, Memory... args) {
        return eof ? Memory.TRUE : Memory.FALSE;
    }

    @Override
    @Signature
    public Memory seek(Environment env, Memory... args) throws IOException {
        throw new IOException("Cannot seek");
    }

    @Override
    @Signature
    public Memory getPosition(Environment env, Memory... args) {
        return LongMemory.valueOf(position);
    }

    @Override
    @Signature
    public Memory close(Environment env, Memory... args) throws IOException {
        stream.close();
        return Memory.NULL;
    }

    @Signature(@Arg("name"))
    public static Memory getResources(Environment env, Memory... args) throws IOException {
        Enumeration<URL> list = Thread.currentThread().getContextClassLoader().getResources(args[0].toString());

        ArrayMemory r = new ArrayMemory();
        while (list.hasMoreElements()) {
            URL url = list.nextElement();
            if (url != null) {
                ResourceStream rs = new ResourceStream(env, url.openStream());
                rs.setPath("res://" + args[0]);

                r.add(rs);
            }
        }

        return r.toConstant();
    }
}
