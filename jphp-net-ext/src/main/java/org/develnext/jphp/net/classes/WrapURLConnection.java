package org.develnext.jphp.net.classes;

import org.develnext.jphp.net.NetExtension;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.annotation.Reflection.WrapInterface;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URLConnection;

@Name(NetExtension.NAMESPACE + "URLConnection")
@WrapInterface(value = URLConnection.class, skipConflicts = true)
public class WrapURLConnection extends BaseWrapper<URLConnection> {
    public WrapURLConnection(Environment env, URLConnection wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapURLConnection(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    protected void __construct() { }

    @Signature
    public void disconnect() {
        ((HttpURLConnection)getWrappedObject()).disconnect();
    }

    @Signature
    public int getResponseCode() throws IOException {
        return ((HttpURLConnection)getWrappedObject()).getResponseCode();
    }

    @Signature
    public String getResponseMessage() throws IOException {
        return ((HttpURLConnection)getWrappedObject()).getResponseMessage();
    }

    @Signature
    public boolean getInstanceFollowRedirects() throws IOException {
        return ((HttpURLConnection)getWrappedObject()).getInstanceFollowRedirects();
    }

    @Signature
    public void setInstanceFollowRedirects(boolean value) throws IOException {
        ((HttpURLConnection)getWrappedObject()).setInstanceFollowRedirects(value);
    }

    @Signature
    public void setChunkedStreamingMode(int chunklen) throws IOException {
        ((HttpURLConnection)getWrappedObject()).setChunkedStreamingMode(chunklen);
    }

    @Signature
    public void setRequestMethod(String method) throws IOException {
        ((HttpURLConnection)getWrappedObject()).setRequestMethod(method);
    }

    @Signature
    public String getRequestMethod() {
        return ((HttpURLConnection)getWrappedObject()).getRequestMethod();
    }

    @Signature
    public boolean usingProxy() {
        return ((HttpURLConnection)getWrappedObject()).usingProxy();
    }
}
