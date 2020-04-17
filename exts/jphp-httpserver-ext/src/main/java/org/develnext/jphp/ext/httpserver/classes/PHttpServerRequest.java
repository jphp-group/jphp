package org.develnext.jphp.ext.httpserver.classes;

import org.develnext.jphp.ext.httpserver.HttpServerExtension;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.util.MultiMap;
import php.runtime.Memory;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.stream.MiscStream;
import php.runtime.ext.core.classes.stream.Stream;
import php.runtime.lang.BaseObject;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.ReferenceMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.*;

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

    public Request getRequest() {
        return request;
    }

    @Signature
    protected void __construct() {
    }

    @Signature
    public String authType() {
        return request.getAuthType();
    }

    @Signature
    public String httpVersion() {
        return request.getHttpVersion().asString();
    }

    @Signature
    public String protocol() {
        return request.getProtocol();
    }

    @Signature
    public Memory headers() {
        return headers(false);
    }

    @Signature
    public Memory headers(boolean lowerKeys) {
        Enumeration<String> headerNames = request.getHeaderNames();
        ArrayMemory result = ArrayMemory.createHashed(15);

        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();

            if (lowerKeys) {
                key = key.toLowerCase();
            }

            ArrayMemory value = ArrayMemory.ofStringEnumeration(request.getHeaders(key));

            if (value.size() == 0) {
                result.putAsKeyString(key, Memory.NULL);
            } else if (value.size() == 1) {
                result.putAsKeyString(key, value.shift());
            } else {
                result.putAsKeyString(key, value);
            }
        }

        return result.toConstant();
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
    public Object attribute(String name) {
        return request.getAttribute(name);
    }

    @Signature
    public void attribute(String name, Object value) {
        request.setAttribute(name, value);
    }

    @Signature
    public String query() {
        return request.getQueryString();
    }

    @Signature
    public String query(String name) {
        request.getParameterMap();
        MultiMap<String> queryParameters = request.getQueryParameters();

        if (queryParameters != null) {
            return queryParameters.getString(name);
        } else {
            return null;
        }
    }

    @Signature
    public String queryEncoding() {
        return request.getQueryEncoding();
    }

    @Signature
    public Memory queryParameters() {
        request.getParameterMap();

        MultiMap<String> parameters = request.getQueryParameters();

        if (parameters != null) {
            ArrayMemory result = ArrayMemory.createHashed(parameters.size());

            for (Map.Entry<String, List<String>> entry : parameters.entrySet()) {
                List<String> value = entry.getValue();

                if (value == null || value.isEmpty()) {
                    result.putAsKeyString(entry.getKey(), Memory.NULL);
                } else if (value.size() == 1) {
                    result.putAsKeyString(entry.getKey(), StringMemory.valueOf(value.get(0)));
                } else {
                    result.putAsKeyString(entry.getKey(), ArrayMemory.ofStringCollection(value));
                }
            }

            return result;
        } else {
            return new ArrayMemory().toConstant();
        }
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
    public Stream bodyStream(Environment env) throws IOException {
        return new MiscStream(env, request.getInputStream());
    }

    @Signature
    public String localAddress() {
        return request.getLocalAddr();
    }

    @Signature
    public int localPort() {
        return request.getLocalPort();
    }

    @Signature
    public String localName() {
        return request.getLocalName();
    }

    @Signature
    public String remoteAddress() {
        return request.getRemoteAddr();
    }

    @Signature
    public String remoteUser() {
        return request.getRemoteUser();
    }

    @Signature
    public String remoteHost() {
        return request.getRemoteHost();
    }

    @Signature
    public int remotePort() {
        return request.getRemotePort();
    }

    @Signature
    public Locale locale() {
        return request.getLocale();
    }

    @Signature
    public Enumeration<Locale> locales() {
        return request.getLocales();
    }

    @Signature
    public Memory cookie(Environment env, String name) {
        Cookie[] cookies = request.getCookies();

        for (Cookie cookie : cookies) {
            if (cookie.getName().equalsIgnoreCase(name)) {
                return ArrayMemory.ofBean(env, cookie).toConstant();
            }
        }

        return Memory.NULL;
    }

    @Signature
    public Memory cookies(Environment env) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            ArrayMemory result = ArrayMemory.createHashed(cookies.length);
            for (Cookie cookie : cookies) {
                if (result.containsKey(cookie.getName())) {
                    ReferenceMemory list = result.getByScalar(cookie.getName());

                    if (list.isArray()) {
                        list.refOfPush().assign(ArrayMemory.ofBean(env, cookie));
                    } else {
                        result.putAsKeyString(cookie.getName(), ArrayMemory.of(list.toValue(), ArrayMemory.ofBean(env, cookie)));
                    }
                } else {
                    result.putAsKeyString(cookie.getName(), ArrayMemory.ofBean(env, cookie));
                }
            }

            return result;
        } else {
            return new ArrayMemory().toConstant();
        }
    }

    @Signature
    public void end() {
        request.setHandled(true);
    }

    @Signature
    public Collection<Part> getParts() throws IOException, ServletException {
        request.setAttribute(Request.MULTIPART_CONFIG_ELEMENT, new MultipartConfigElement(""));
        return request.getParts();
    }
}
