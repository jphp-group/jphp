package org.develnext.jphp.ext.httpserver.classes;

import org.develnext.jphp.ext.httpserver.HttpServerExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Nullable;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.stream.MiscStream;
import php.runtime.ext.core.classes.stream.Stream;
import php.runtime.lang.BaseObject;
import php.runtime.memory.ArrayMemory;
import php.runtime.reflection.ClassEntity;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;

@Name("HttpServerResponse")
@Namespace(HttpServerExtension.NS)
public class PHttpServerResponse extends BaseObject {
    private HttpServletResponse response;

    public PHttpServerResponse(Environment env, HttpServletResponse response) {
        super(env);
        this.response = response;
    }

    public PHttpServerResponse(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    @Signature
    protected void __construct() {
    }

    @Signature
    public PHttpServerResponse write(Memory value) throws IOException {
        write(value, "UTF-8");
        return this;
    }

    @Signature
    public PHttpServerResponse write(Memory value, String charset) throws IOException {
        response.getOutputStream().write(value.getBinaryBytes(Charset.forName(charset)));
        return this;
    }

    @Signature
    public PHttpServerResponse body(Memory value) throws IOException {
        return body(value, "UTF-8");
    }

    @Signature
    public PHttpServerResponse body(Memory value, String charset) throws IOException {
        write(value, charset);
        response.getOutputStream().close();
        return this;
    }

    @Signature
    public PHttpServerResponse status(int status) throws IOException {
        status(status, null);
        return this;
    }

    @Signature
    public PHttpServerResponse status(int status, @Nullable String message) throws IOException {
        response.setStatus(status);

        if (message != null && !message.isEmpty()) {
            response.sendError(status, message);
        }

        return this;
    }

    @Signature
    public PHttpServerResponse header(String name, Memory value) {
        response.addHeader(name, value.toString());
        return this;
    }

    @Signature
    public PHttpServerResponse contentType(String value) {
        response.setContentType(value);
        return this;
    }

    @Signature
    public PHttpServerResponse contentLength(long value) {
        response.setContentLengthLong(value);
        return this;
    }

    @Signature
    public PHttpServerResponse redirect(String value) throws IOException {
        response.sendRedirect(value);
        return this;
    }

    @Signature
    public PHttpServerResponse flush() throws IOException {
        response.flushBuffer();
        return this;
    }

    @Signature
    public String encodeRedirectUrl(String value) {
        return response.encodeRedirectURL(value);
    }

    @Signature
    public String encodeUrl(String url) {
        return response.encodeURL(url);
    }

    @Signature
    public Stream bodyStream(Environment env) throws IOException {
        return new MiscStream(env, response.getOutputStream());
    }

    @Signature
    public PHttpServerResponse locale(Locale locale) {
        response.setLocale(locale);
        return this;
    }

    @Signature
    public PHttpServerResponse charsetEncoding(String encoding) {
        response.setCharacterEncoding(encoding);
        return this;
    }

    @Signature
    public PHttpServerResponse addCookie(Environment env, ArrayMemory cookie) {
        Cookie _cookie = new Cookie(cookie.valueOfIndex("name").toString(), cookie.valueOfIndex("value").toString());
        cookie.toBean(env, _cookie);

        response.addCookie(_cookie);
        return this;
    }
}
