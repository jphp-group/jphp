package org.develnext.jphp.ext.webserver.classes;

import org.develnext.jphp.ext.webserver.WebServerExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.memory.ArrayMemory;
import php.runtime.reflection.ClassEntity;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Abstract
@Reflection.Name("WebResponse")
@Reflection.Namespace(WebServerExtension.NS)
public class PWebResponse extends BaseWrapper<HttpServletResponse> {
    interface WrappedInterface {
        @Property int status();

        void setHeader(String name, @Nullable String value);
        void addHeader(String name, String value);
        String getHeader(String name);
        boolean containsHeader(String name);
        Collection<String> getHeaders(String name);
        Collection<String> getHeaderNames();

        String encodeRedirectURL(String url);
    }

    public PWebResponse(Environment env, HttpServletResponse wrappedObject) {
        super(env, wrappedObject);
        wrappedObject.setCharacterEncoding(env.getDefaultCharset().name());
    }

    public PWebResponse(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    protected void __construct(PWebResponse parent) {
        this.__wrappedObject = parent.getWrappedObject();
    }

    @Signature
    public void redirect(String location) throws IOException {
        redirect(location, 301);
    }

    @Signature
    public void redirect(String location, int httpStatusCode) throws IOException {
        getWrappedObject().setStatus(httpStatusCode);
        getWrappedObject().setHeader("Location", location);
        getWrappedObject().setHeader("Connection", "close");

        getWrappedObject().sendError(httpStatusCode);
    }

    @Signature
    public void writeToBody(Environment env, Memory value) throws IOException {
        getWrappedObject().getOutputStream().write(value.getBinaryBytes(env.getDefaultCharset()));
    }

    @Setter
    protected void setContentType(String value) {
        getWrappedObject().setContentType(value);
    }

    @Getter
    protected String getContentType() {
        return getWrappedObject().getContentType();
    }

    @Setter
    protected void setCharacterEncoding(String value) {
        getWrappedObject().setCharacterEncoding(value);
    }

    @Getter
    protected String getCharacterEncoding() {
        return getWrappedObject().getCharacterEncoding();
    }

    @Setter
    protected void setBufferSize(int value) {
        getWrappedObject().setBufferSize(value);
    }

    @Getter
    protected int getBufferSize() {
        return getWrappedObject().getBufferSize();
    }

    @Signature
    public void setContentLength(int value) {
        getWrappedObject().setContentLength(value);
    }

    @Signature
    public void addCookie(ArrayMemory cookie) {
        Cookie _cookie = new Cookie(cookie.valueOfIndex("name").toString(), cookie.valueOfIndex("value").toString());

        if (cookie.containsKey("path")) {
            _cookie.setPath(cookie.valueOfIndex("path").toString());
        }

        if (cookie.containsKey("domain")) {
            _cookie.setDomain(cookie.valueOfIndex("domain").toString());
        }

        if (cookie.containsKey("httpOnly")) {
            _cookie.setHttpOnly(cookie.valueOfIndex("httpOnly").toBoolean());
        }

        if (cookie.containsKey("maxAge")) {
            _cookie.setMaxAge(cookie.valueOfIndex("maxAge").toInteger());
        }

        if (cookie.containsKey("secure")) {
            _cookie.setSecure(cookie.valueOfIndex("secure").toBoolean());
        }

        if (cookie.containsKey("comment")) {
            _cookie.setComment(cookie.valueOfIndex("comment").toString());
        }

        if (cookie.containsKey("version")) {
            _cookie.setVersion(cookie.valueOfIndex("version").toInteger());
        }

        getWrappedObject().addCookie(_cookie);
    }

    @Signature
    public static PWebResponse current(Environment env) {
        return env.getUserValue(PWebResponse.class);
    }
}
