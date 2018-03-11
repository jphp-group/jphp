package org.develnext.jphp.ext.mail.classes;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.develnext.jphp.ext.mail.MailExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Getter;
import php.runtime.annotation.Reflection.Setter;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

@Reflection.Namespace(MailExtension.NS)
@Reflection.Name("EmailBackend")
public class PEmailBackend extends BaseObject {
    protected MultiPartEmail helper;

    public PEmailBackend(Environment env) {
        super(env);
    }

    public PEmailBackend(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        helper = new MultiPartEmail();
    }

    @Setter
    protected void setHostName(String value) {
        helper.setHostName(value);
    }

    @Getter
    protected String getHostName() {
        return helper.getHostName();
    }

    @Setter
    protected void setSmtpPort(Memory value) {
        helper.setSmtpPort(value.toInteger());
    }

    @Getter
    protected Memory getSmtpPort() {
        return StringMemory.valueOf(helper.getSmtpPort()).toNumeric();
    }

    @Setter
    protected void setSslSmtpPort(String value) {
        helper.setSslSmtpPort(value);
    }

    @Getter
    protected String getSslSmtpPort() {
        return helper.getSslSmtpPort();
    }

    @Setter
    protected void setSendPartial(boolean value) {
        helper.setSendPartial(value);
    }

    @Getter
    protected boolean isSendPartial() {
        return helper.isSendPartial();
    }

    @Signature
    public void setAuthentication(String login, String password) {
        helper.setAuthentication(login, password);
    }

    @Signature
    public void clearAuthentication() {
        helper.setAuthenticator(null);
    }

    @Setter
    protected void setSocketTimeout(int timeout) {
        helper.setSocketTimeout(timeout);
    }

    @Getter
    protected int getSocketTimeout() {
        return helper.getSocketTimeout();
    }

    @Setter
    protected void setSocketConnectionTimeout(int timeout) {
        helper.setSocketConnectionTimeout(timeout);
    }

    @Getter
    protected int getSocketConnectionTimeout() {
        return helper.getSocketConnectionTimeout();
    }

    @Setter
    protected void setSslOnConnect(boolean value) {
        helper.setSSLOnConnect(value);
    }

    @Getter
    protected boolean isSslOnConnect() {
        return helper.isSSLOnConnect();
    }

    @Setter
    protected void setSslCheckServerIdentity(boolean value) {
        helper.setSSLCheckServerIdentity(value);
    }

    @Getter
    protected boolean isSslCheckServerIdentity() {
        return helper.isSSLCheckServerIdentity();
    }

    public void _apply(Email email) throws EmailException {
        email.setMailSession(helper.getMailSession());
    }
}
