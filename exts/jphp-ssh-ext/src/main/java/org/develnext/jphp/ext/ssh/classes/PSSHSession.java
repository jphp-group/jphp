package org.develnext.jphp.ext.ssh.classes;

import com.jcraft.jsch.*;
import org.develnext.jphp.ext.ssh.SSHExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Arg;
import php.runtime.annotation.Reflection.Nullable;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseWrapper;
import php.runtime.memory.ArrayMemory;
import php.runtime.reflection.ClassEntity;

import static php.runtime.common.HintType.ARRAY;

@Reflection.Name("SSHSession")
@Reflection.Namespace(SSHExtension.NS)
public class PSSHSession extends BaseWrapper<Session> {
    static class UserInfoBean implements UserInfo {
        private Invoker getPassphrase;
        private Invoker getPassword;
        private Invoker promptPassword;
        private Invoker promptPassphrase;
        private Invoker promptYesNo;
        private Invoker showMessage;

        private ArrayMemory source;

        public UserInfoBean() {
        }

        public UserInfoBean(ArrayMemory source) {
            this.source = source;
        }

        public Invoker getGetPassphrase() {
            return getPassphrase;
        }

        public void setGetPassphrase(Invoker getPassphrase) {
            this.getPassphrase = getPassphrase;
        }

        public Invoker getGetPassword() {
            return getPassword;
        }

        public void setGetPassword(Invoker getPassword) {
            this.getPassword = getPassword;
        }

        public Invoker getPromptPassword() {
            return promptPassword;
        }

        public void setPromptPassword(Invoker promptPassword) {
            this.promptPassword = promptPassword;
        }

        public Invoker getPromptPassphrase() {
            return promptPassphrase;
        }

        public void setPromptPassphrase(Invoker promptPassphrase) {
            this.promptPassphrase = promptPassphrase;
        }

        public Invoker getPromptYesNo() {
            return promptYesNo;
        }

        public void setPromptYesNo(Invoker promptYesNo) {
            this.promptYesNo = promptYesNo;
        }

        public Invoker getShowMessage() {
            return showMessage;
        }

        public void setShowMessage(Invoker showMessage) {
            this.showMessage = showMessage;
        }

        @Override
        public String getPassphrase() {
            return getPassphrase == null ? null : getPassphrase.callAny().toString();
        }

        @Override
        public String getPassword() {
            return getPassword == null ? null : getPassword.callAny().toString();
        }

        @Override
        public boolean promptPassword(String message) {
            return promptPassword != null && promptPassword.callAny(message).toBoolean();
        }

        @Override
        public boolean promptPassphrase(String message) {
            return promptPassphrase != null && promptPassphrase.callAny(message).toBoolean();
        }

        @Override
        public boolean promptYesNo(String message) {
            return promptYesNo != null && promptYesNo.callAny(message).toBoolean();
        }

        @Override
        public void showMessage(String message) {
            if (showMessage != null) {
                showMessage.callAny(message);
            }
        }
    }

    interface WrappedInterface {
        @Property String clientVersion();
        @Property String host();
        @Property int port();
        @Property int timeout();

        void setConfig(String key, String value);
        String getConfig(String key);

        void connect();
        void connect(int timeout);

        void disconnect();

        void sendIgnore();
        void sendKeepAliveMsg();
        void rekey();

        void setPassword(String password);
        void setDaemonThread(boolean enable);
    }

    public PSSHSession(Environment env, Session wrappedObject) {
        super(env, wrappedObject);
    }

    public PSSHSession(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    private void __construct() {
    }

    @Signature
    public void setUserInfo(Environment env, @Nullable @Arg(type = ARRAY) Memory ui) {
        if (ui.isNull()) {
            getWrappedObject().setUserInfo(null);
        } else {
            UserInfoBean userInfo = ui.toValue(ArrayMemory.class).toBean(env, UserInfoBean.class);
            getWrappedObject().setUserInfo(userInfo);
        }
    }

    @Signature
    public Memory getUserInfo() {
        UserInfo userInfo = getWrappedObject().getUserInfo();

        if (userInfo instanceof UserInfoBean) {
            return ((UserInfoBean) userInfo).source;
        }

        return null;
    }

    @Signature
    public PSSHExecChannel exec(Environment env) throws JSchException {
        return new PSSHExecChannel(env, (ChannelExec) getWrappedObject().openChannel("exec"));
    }

    @Signature
    public PSSHSftpChannel sftp(Environment env) throws JSchException {
        return new PSSHSftpChannel(env, (ChannelSftp) getWrappedObject().openChannel("sftp"));
    }
}
