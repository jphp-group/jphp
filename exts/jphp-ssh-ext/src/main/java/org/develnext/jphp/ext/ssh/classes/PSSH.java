package org.develnext.jphp.ext.ssh.classes;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.OpenSSHConfig;
import com.jcraft.jsch.Session;
import org.develnext.jphp.ext.ssh.SSHExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

import java.io.InputStream;

@Reflection.Name("SSH")
@Reflection.Namespace(SSHExtension.NS)
public class PSSH extends BaseWrapper<JSch> {
    interface WrappedInterface {
        Session getSession(String username, String host);
        Session getSession(String username, String host, int port);
        void setKnownHosts(InputStream stream);
        void addIdentity(String prvkey, String passphrase);
        void removeAllIdentity();
    }

    public PSSH(Environment env, JSch wrappedObject) {
        super(env, wrappedObject);
    }

    public PSSH(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new JSch();
    }

    @Signature
    public Session openSession(String host) throws JSchException {
        return getWrappedObject().getSession(host);
    }

    @Signature
    public Session openSession(String host, int port) throws JSchException {
        return getWrappedObject().getSession(null, host, port);
    }
}
