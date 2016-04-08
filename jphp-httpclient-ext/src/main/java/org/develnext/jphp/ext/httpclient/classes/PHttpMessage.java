package org.develnext.jphp.ext.httpclient.classes;

import org.develnext.jphp.ext.httpclient.HttpClientExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.memory.ArrayMemory;
import php.runtime.reflection.ClassEntity;

@Reflection.Name("HttpMessage")
@Reflection.Namespace(HttpClientExtension.NS)
public class PHttpMessage extends BaseObject {
    protected ArrayMemory headers = new ArrayMemory(true);
    protected ArrayMemory cookies = new ArrayMemory(true);

    protected Memory body = Memory.NULL;

    public PHttpMessage(Environment env) {
        super(env);
    }

    public PHttpMessage(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public ArrayMemory getHeaders() {
        return headers;
    }

    @Signature
    public void setHeaders(ArrayMemory headers) {
        this.headers = headers;
    }

    @Signature
    public Memory getBody(Environment env) throws Throwable {
        return body;
    }

    @Signature
    public void setBody(Memory body) {
        this.body = body;
    }

    @Signature
    public boolean hasHeader(String name) {
        return headers.containsKey(name);
    }

    @Signature
    public Memory getHeader(String name) {
        return headers.valueOfIndex(name);
    }

    @Signature
    public void setHeader(String name, Memory value) {
        headers.putAsKeyString(name, value.toImmutable());
    }

    @Signature
    public boolean removeHeader(String name) {
        return headers.removeByScalar(name) != null;
    }

    @Signature
    public ArrayMemory getCookies() {
        return cookies;
    }

    @Signature
    public void setCookies(ArrayMemory cookies) {
        this.cookies = cookies;
    }

    @Signature
    public boolean hasCookie(String name) {
        return cookies.containsKey(name);
    }

    @Signature
    public Memory getCookie(String name) {
        return cookies.valueOfIndex(name);
    }

    @Signature
    public boolean removeCookie(String name) {
        return cookies.removeByScalar(name) != null;
    }
}
