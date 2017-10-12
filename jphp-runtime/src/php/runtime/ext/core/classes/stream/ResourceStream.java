package php.runtime.ext.core.classes.stream;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.BinaryMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.StringMemory;
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
    
    public InputStream getInputStream() {
        return this.stream;
    }

    @Override
    @Signature({@Arg("path")})
    public Memory __construct(Environment env, Memory... args) throws IOException {
        super.__construct(env, args[0], StringMemory.valueOf("r"));
        //long t = System.currentTimeMillis();

        String path = this.getPath().replace('\\', '/').replace("//", "/");

        if (path.startsWith("/")) {
            path = path.substring(1);
        }

        setPath("res://" + path);

        url = env.getScope().getClassLoader().getResource(path);

        if (url == null) {
            throw new IOException("Resource not found - " + getPath());
        }

        stream = url.openStream();

        //System.err.println(path + ", time = " + (System.currentTimeMillis() - t));

        return Memory.NULL;
    }

    @Signature
    public Memory toExternalForm(Environment env, Memory... args) {
        return StringMemory.valueOf(url.toExternalForm());
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

        byte[] buff = new byte[len];
        int read;
        read = stream.read(buff, 0, len);

        eof = read == -1;
        if (read == -1)
            return Memory.NULL;

        position += read;

        if (read != buff.length){
            buff = Arrays.copyOf(buff, read);
        }

        return new BinaryMemory(buff);
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
    public static Memory exists(Environment env, Memory... args) throws IOException {
        String name = args[0].toString();

        if (name.startsWith("res:///")) {
            name = name.substring(7);
        } else if (name.startsWith("res://")) {
            name = name.substring(6);
        }

        return env.getScope().getClassLoader().getResource(name) == null ? Memory.FALSE : Memory.TRUE;
    }

    @Signature(@Arg("name"))
    public static Memory getResources(Environment env, Memory... args) throws IOException {
        Enumeration<URL> list = env.getScope().getClassLoader().getResources(args[0].toString());

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
