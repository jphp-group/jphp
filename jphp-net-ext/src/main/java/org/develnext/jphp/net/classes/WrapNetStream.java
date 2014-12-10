package org.develnext.jphp.net.classes;

import org.develnext.jphp.net.NetExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection.*;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.stream.Stream;
import php.runtime.invoke.Invoker;
import php.runtime.memory.BinaryMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.reflection.ClassEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.Arrays;

@Name(NetExtension.NAMESPACE + "NetStream")
@Stream.UsePathWithProtocols
public class WrapNetStream extends Stream {
    protected URL url;

    protected Proxy proxy;

    protected boolean eof = false;
    protected int position = 0;

    protected URLConnection urlConnection;

    protected InputStream currentInputStream;
    protected OutputStream currentOutputStream;

    public WrapNetStream(Environment env, URL url) {
        super(env);
        this.url = url;
    }

    public WrapNetStream(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public URLConnection getURLConnection() throws IOException {
        if (urlConnection == null) {
            if (proxy != null) {
                urlConnection = url.openConnection(proxy);
            } else {
                urlConnection = url.openConnection();
            }

            urlConnection.setDoInput(false);
            urlConnection.setDoOutput(false);

            if (getMode().startsWith("r") || getMode().startsWith("w") || getMode().startsWith("a")) {
                urlConnection.setDoInput(true);
            }

            if (getMode().startsWith("w") || getMode().startsWith("a")) {
                urlConnection.setDoOutput(true);
            }
        }

        return urlConnection;
    }

    public HttpURLConnection getHttpURLConnection() throws IOException {
        return (HttpURLConnection) getURLConnection();
    }

    public OutputStream getOutputStream() throws IOException {
        if (currentOutputStream == null) {
            currentOutputStream = getURLConnection().getOutputStream();
        }

        return currentOutputStream;
    }

    public InputStream getInputStream() throws IOException {
        if (currentInputStream == null) {
            currentInputStream = getURLConnection().getInputStream();
        }

        return currentInputStream;
    }

    @Override
    @Signature({@Arg("path"), @Arg(value = "mode", optional = @Optional("w+"))})
    public Memory __construct(Environment env, Memory... args) throws IOException {
        super.__construct(env, args);

        if (getMode() == null) {
            setMode("w+");
        }

        url = new URL(args[0].toString());

        return Memory.NULL;
    }

    @Signature
    public WrapURL getURL(Environment env) throws MalformedURLException {
        return new WrapURL(env, new URL(getPath()));
    }

    @Override
    @Signature
    public Memory write(Environment env, Memory... args) throws IOException {
        int len      = args[1].toInteger();
        byte[] bytes = args[0].getBinaryBytes();

        len          = len == 0 || len > bytes.length ? bytes.length : len;

        getOutputStream().write(bytes, 0, len);

        return LongMemory.valueOf(len);
    }

    @Override
    @Signature
    public Memory read(Environment env, Memory... args) throws IOException {
        int len = args[0].toInteger();
        if (len <= 0)
            return Memory.FALSE;

        byte[] buf = new byte[len];
        int read;
        read = getInputStream().read(buf);
        eof  = read == -1;

        if (read == -1)
            return Memory.FALSE;

        position += read;
        return new BinaryMemory(Arrays.copyOf(buf, read));
    }

    @Override
    @Signature({
            @Arg(value = "bufferSize", optional = @Optional("4096"))
    })
    public Memory readFully(Environment env, Memory... args) throws IOException {
        final int bufferSize = args[0].toInteger();

        if (bufferSize <= 0) {
            return Memory.FALSE;
        }

        byte[] buff = new byte[bufferSize];
        int len;

        ByteArrayOutputStream tmp = new ByteArrayOutputStream();
        while ((len = getInputStream().read(buff)) > 0) {
            tmp.write(buff, 0, len);

            position += len;
        }

        return new BinaryMemory(tmp.toByteArray());
    }

    @Override
    @Signature
    public Memory eof(Environment env, Memory... args) {
        if (urlConnection == null) {
            return Memory.FALSE;
        }

        return eof ? Memory.TRUE : Memory.FALSE;
    }

    @Override
    @Signature
    public Memory seek(Environment env, Memory... args) throws IOException {
        throw new IOException("Unavailable seek() operation");
    }

    @Override
    @Signature
    public Memory getPosition(Environment env, Memory... args) {
        return LongMemory.valueOf(position);
    }

    @Override
    @Signature
    public Memory close(Environment env, Memory... args) throws IOException {
        if (urlConnection != null && urlConnection instanceof HttpURLConnection) {
            ((HttpURLConnection) urlConnection).disconnect();
        }

        return Memory.NULL;
    }

    @Signature
    public void setProxy(@Nullable Proxy proxy) {
        this.proxy = proxy;
    }

    @Signature
    public Proxy getProxy() {
        return this.proxy;
    }

    @Signature
    public URLConnection getUrlConnection() throws IOException {
        return getURLConnection();
    }
}
