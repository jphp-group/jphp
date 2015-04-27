package org.develnext.jphp.ext.webserver.classes;

import org.develnext.jphp.ext.webserver.WebServerExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Abstract;
import php.runtime.annotation.Reflection.Getter;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.stream.MiscStream;
import php.runtime.ext.core.classes.stream.Stream;
import php.runtime.lang.BaseWrapper;
import php.runtime.memory.ArrayMemory;
import php.runtime.reflection.ClassEntity;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

@Abstract
@Reflection.Name("WebRequest")
@Reflection.Namespace(WebServerExtension.NS)
public class PWebRequest extends BaseWrapper<HttpServletRequest> {
    interface WrappedInterface {
        @Property String authType();
        @Property String method();
        @Property String scheme();
        @Property String pathInfo();
        @Property String pathTranslated();
        @Property String contextPath();
        @Property String queryString();
        @Property String remoteUser();
        @Property String servletPath();
    }

    protected String body;

    public PWebRequest(Environment env, HttpServletRequest wrappedObject) {
        super(env, wrappedObject);
    }

    public PWebRequest(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    protected void __construct(PWebRequest parent) {
        this.__wrappedObject = parent.getWrappedObject();
    }

    @Getter
    protected String getHost() {
        return getWrappedObject().getHeader("host");
    }

    @Getter
    protected String getUserAgent() {
        return getWrappedObject().getHeader("user-agent");
    }

    @Getter
    protected int getPort() {
        return getWrappedObject().getServerPort();
    }

    @Getter
    protected String getUrl() {
        return getWrappedObject().getRequestURL().toString();
    }

    @Getter
    protected String getIp() {
        return getWrappedObject().getRemoteAddr();
    }

    @Signature
    public Stream getBodyStream(Environment env) throws IOException {
        return new MiscStream(env, getWrappedObject().getInputStream());
    }

    @Getter
    public Memory getCookies() {
        Cookie[] cookies = getWrappedObject().getCookies();

        ArrayMemory result = new ArrayMemory();

        for (Cookie cookie : cookies) {
            ArrayMemory item = new ArrayMemory();

            item.refOfIndex("name").assign(cookie.getName());
            item.refOfIndex("value").assign(cookie.getValue());
            item.refOfIndex("path").assign(cookie.getPath());
            item.refOfIndex("domain").assign(cookie.getDomain());
            item.refOfIndex("maxAge").assign(cookie.getMaxAge());
            item.refOfIndex("httpOnly").assign(cookie.isHttpOnly());
            item.refOfIndex("secure").assign(cookie.getSecure());
            item.refOfIndex("comment").assign(cookie.getComment());

            result.add(item);
        }

        return result.toConstant();
    }

    @Signature
    public String getBody(Environment env) throws IOException {
        if (body != null) {
            return body;
        }

        StringBuffer jb = new StringBuffer();
        String line = null;

        BufferedReader reader = getWrappedObject().getReader();
        while ((line = reader.readLine()) != null) {
            jb.append(line);
        }

        return body = jb.toString();
    }
}
