package org.develnext.jphp.ext.ssh.classes;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import org.develnext.jphp.ext.ssh.SSHExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Nullable;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.stream.Stream;
import php.runtime.reflection.ClassEntity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Reflection.Name("SSHExecChannel")
@Reflection.Namespace(SSHExtension.NS)
public class PSSHExecChannel extends PSSHChannel<ChannelExec> {
    public PSSHExecChannel(Environment env, ChannelExec wrappedObject) {
        super(env, wrappedObject);
    }

    public PSSHExecChannel(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void command(String command) {
        getWrappedObject().setCommand(command);
    }

    @Signature
    public InputStream getErrorStream() throws IOException {
        return getWrappedObject().getErrStream();
    }

    @Signature
    public void setErrorStream(Environment env, @Nullable Stream stream) {
        setErrorStream(env, stream, false);
    }

    @Signature
    public void setErrorStream(Environment env, @Nullable Stream stream, boolean dontClose) {
        getWrappedObject().setErrStream(stream == null ? null : Stream.getOutputStream(env, stream), dontClose);
    }
}
