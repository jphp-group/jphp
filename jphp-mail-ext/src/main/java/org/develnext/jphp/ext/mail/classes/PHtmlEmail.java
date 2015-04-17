package org.develnext.jphp.ext.mail.classes;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.develnext.jphp.ext.mail.MailExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Nullable;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.stream.Stream;
import php.runtime.lang.BaseObject;
import php.runtime.reflection.ClassEntity;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.util.ByteArrayDataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Reflection.Name("Email")
@Reflection.Namespace(MailExtension.NS)
public class PHtmlEmail extends BaseObject {
    private HtmlEmail htmlEmail;

    public PHtmlEmail(Environment env) {
        super(env);
    }

    public PHtmlEmail(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        htmlEmail = new HtmlEmail();
    }

    @Signature
    public PHtmlEmail setHtmlMessage(String value) throws EmailException {
        htmlEmail.setHtmlMsg(value);
        return this;
    }

    @Signature
    public PHtmlEmail setTextMessage(String value) throws EmailException {
        htmlEmail.setTextMsg(value);
        return this;
    }

    @Signature
    public PHtmlEmail setMessage(String value) throws EmailException {
        htmlEmail.setMsg(value);
        return this;
    }

    @Signature
    public PHtmlEmail setFrom(String email, String name, String charset) throws EmailException {
        htmlEmail.setFrom(email, name, charset);
        return this;
    }

    @Signature
    public PHtmlEmail setFrom(String email, String name) throws EmailException {
        htmlEmail.setFrom(email, name);
        return this;
    }

    @Signature
    public PHtmlEmail setFrom(String email) throws EmailException {
        htmlEmail.setFrom(email);
        return this;
    }

    @Signature
    public PHtmlEmail setCharset(String charset) {
        htmlEmail.setCharset(charset);
        return this;
    }

    @Signature
    public PHtmlEmail setSubject(String subject) {
        htmlEmail.setSubject(subject);
        return this;
    }

    @Signature
    public PHtmlEmail setTo(List<InternetAddress> addresses) throws AddressException, EmailException {
        htmlEmail.setTo(addresses);
        return this;
    }

    @Signature
    public PHtmlEmail setCc(List<InternetAddress> addresses) throws EmailException {
        htmlEmail.setCc(addresses);
        return this;
    }

    @Signature
    public PHtmlEmail setBcc(List<InternetAddress> addresses) throws EmailException {
        htmlEmail.setBcc(addresses);
        return this;
    }

    @Signature
    public PHtmlEmail setBounceAddress(@Nullable String email) throws EmailException {
        htmlEmail.setBounceAddress(email);
        return this;
    }

    @Signature
    public PHtmlEmail setHeaders(Map<String, String> map) {
        htmlEmail.setHeaders(map);
        return this;
    }

    @Signature
    public PHtmlEmail attach(Environment env, Memory content, String type, String name, String description)
            throws EmailException, MessagingException, IOException {
        InputStream is = Stream.getInputStream(env, content);

        try {
            htmlEmail.attach(new ByteArrayDataSource(is, type), name, description, EmailAttachment.ATTACHMENT);
            return this;
        } finally {
            Stream.closeStream(env, is);
        }
    }

    @Signature
    public PHtmlEmail attach(Environment env, Memory content, String type, String name)
            throws EmailException, MessagingException, IOException {
        return attach(env, content, type, name, "");
    }

    @Signature
    public String send(PEmailBackend backend) throws EmailException {
        backend._apply(htmlEmail);
        return htmlEmail.send();
    }
}
