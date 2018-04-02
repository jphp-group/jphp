package org.develnext.jphp.ext.ssh;

import com.jcraft.jsch.*;
import org.develnext.jphp.ext.ssh.classes.*;
import php.runtime.env.CompileScope;
import php.runtime.ext.support.Extension;

public class SSHExtension extends Extension {
    public static final String NS = "ssh";

    @Override
    public Status getStatus() {
        return Status.EXPERIMENTAL;
    }

    @Override
    public String[] getPackageNames() {
        return new String[] { "ssh" };
    }

    @Override
    public void onRegister(CompileScope scope) {
        registerWrapperClass(scope, Channel.class, PSSHChannel.class);
        registerWrapperClass(scope, ChannelExec.class, PSSHExecChannel.class);
        registerWrapperClass(scope, ChannelSftp.class, PSSHSftpChannel.class);
        registerWrapperClass(scope, Session.class, PSSHSession.class);
        registerWrapperClass(scope, JSch.class, PSSH.class);

        registerJavaException(scope, PSSHException.class, JSchException.class, SftpException.class);
    }
}
