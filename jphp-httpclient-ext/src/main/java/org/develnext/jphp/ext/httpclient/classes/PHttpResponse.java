package org.develnext.jphp.ext.httpclient.classes;

import org.develnext.jphp.ext.httpclient.HttpClientExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Nullable;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.stream.Stream;
import php.runtime.lang.ForeachIterator;
import php.runtime.memory.ArrayMemory;
import php.runtime.reflection.ClassEntity;

@Name("HttpResponse")
@Namespace(HttpClientExtension.NS)
public class PHttpResponse extends PHttpMessage {
    protected int statusCode;
    protected String statusMessage;
    protected Stream bodyStream = null;
    protected ArrayMemory rawCookies = new ArrayMemory();

    public PHttpResponse(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public PHttpResponse(Environment env) {
        super(env);
    }

    @Signature
    public int getStatusCode() {
        return statusCode;
    }

    @Signature
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    @Signature
    public String getStatusMessage() {
        return statusMessage;
    }

    @Signature
    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    @Override
    @Signature
    public Memory getBody(Environment env) throws Throwable {
        if (bodyStream != null && body.isNull()) {
            body = env.invokeMethod(bodyStream, "readFully");
        }

        return super.getBody(env);
    }

    @Signature
    public Stream getBodyStream() {
        return bodyStream;
    }

    @Signature
    public void setBodyStream(@Nullable Stream bodyStream) {
        this.bodyStream = bodyStream;
        this.body = Memory.NULL;
    }

    @Signature
    public ArrayMemory getRawCookies() {
        return rawCookies;
    }

    @Signature
    public void setRawCookies(ArrayMemory rawCookies) {
        this.rawCookies = rawCookies;

        ArrayMemory cookies = new ArrayMemory();

        ForeachIterator iterator = rawCookies.foreachIterator(false, false);

        while (iterator.next()) {
            cookies.putAsKeyString(iterator.getStringKey(), iterator.getValue().valueOfIndex("value").toImmutable());
        }

        this.cookies = cookies;
    }
}
