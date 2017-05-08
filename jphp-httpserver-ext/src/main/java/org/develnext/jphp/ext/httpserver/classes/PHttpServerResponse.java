package org.develnext.jphp.ext.httpserver.classes;

import org.develnext.jphp.ext.httpserver.HttpServerExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Nullable;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.reflection.ClassEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;

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
}
