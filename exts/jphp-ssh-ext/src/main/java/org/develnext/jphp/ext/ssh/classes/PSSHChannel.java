package org.develnext.jphp.ext.ssh.classes;

import com.jcraft.jsch.Channel;
import org.develnext.jphp.ext.ssh.SSHExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Nullable;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.stream.Stream;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Reflection.Name("SSHChannel")
@Reflection.Namespace(SSHExtension.NS)
public class PSSHChannel<T extends Channel> extends BaseWrapper<T> {
    interface WrappedInterface {
        @Property int id();
        @Property int exitStatus();

        void connect();
        void connect(int timeout);
        void disconnect();

        boolean isClosed();
        boolean isConnected();
        boolean isEOF();

        void sendSignal(String signal);

        void start();
    }

    public PSSHChannel(Environment env, T wrappedObject) {
        super(env, wrappedObject);
    }

    public PSSHChannel(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public T getWrappedObject() {
        return (T) super.getWrappedObject();
    }

    @Signature
    private void __construct() {
    }

    @Signature
    public InputStream getInputStream() throws IOException {
        return getWrappedObject().getInputStream();
    }

    @Signature
    public void setInputStream(Environment env, @Nullable Stream stream) {
        setInputStream(env, stream, false);
    }

    @Signature
    public void setInputStream(Environment env, @Nullable Stream stream, boolean dontClose) {
        getWrappedObject().setInputStream(stream == null ? null : Stream.getInputStream(env, stream), dontClose);
    }


    @Signature
    public OutputStream getOutputStream() throws IOException {
        return getWrappedObject().getOutputStream();
    }

    @Signature
    public void setOutputStream(Environment env, @Nullable Stream stream) {
        setOutputStream(env, stream, false);
    }

    @Signature
    public void setOutputStream(Environment env, @Nullable Stream stream, boolean dontClose) {
        getWrappedObject().setOutputStream(stream == null ? null : Stream.getOutputStream(env, stream), dontClose);
    }
}
