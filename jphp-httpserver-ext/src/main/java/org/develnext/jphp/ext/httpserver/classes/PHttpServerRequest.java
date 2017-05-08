package org.develnext.jphp.ext.httpserver.classes;

import org.develnext.jphp.ext.httpserver.HttpServerExtension;
import org.eclipse.jetty.server.Request;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.reflection.ClassEntity;

@Name("HttpServerRequest")
@Namespace(HttpServerExtension.NS)
public class PHttpServerRequest extends BaseObject {
    private Request request;

    public PHttpServerRequest(Environment env, Request request) {
        super(env);
        this.request = request;
    }

    public PHttpServerRequest(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    protected void __construct() {
    }

    @Signature
    public String header(String name) {
        return request.getHeader(name);
    }

    @Signature
    public String param(String name) {
        return request.getParameter(name);
    }

    @Signature
    public String query() {
        return request.getQueryString();
    }

    @Signature
    public String path() {
        return request.getPathInfo();
    }

    @Signature
    public String method() {
        return request.getMethod();
    }

    @Signature
    public String sessionId() {
        return request.getSession(true).getId();
    }

    @Signature
    public void end() {
        request.setHandled(true);
    }
}
