package org.develnext.jphp.ext.ssh.classes;

import org.develnext.jphp.ext.ssh.SSHExtension;
import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.ext.java.JavaException;
import php.runtime.reflection.ClassEntity;

@Reflection.Name("SSHException")
@Reflection.Namespace(SSHExtension.NS)
public class PSSHException extends JavaException {
    public PSSHException(Environment env, Throwable throwable) {
        super(env, throwable);
    }

    public PSSHException(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }
}
