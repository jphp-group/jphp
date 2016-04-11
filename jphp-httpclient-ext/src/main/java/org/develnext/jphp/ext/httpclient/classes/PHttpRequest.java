package org.develnext.jphp.ext.httpclient.classes;

import org.develnext.jphp.ext.httpclient.HttpClientExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Environment;
import php.runtime.ext.net.WrapURLConnection;
import php.runtime.lang.BaseObject;
import php.runtime.memory.ArrayMemory;
import php.runtime.reflection.ClassEntity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Name("HttpRequest")
@Namespace(HttpClientExtension.NS)
public class PHttpRequest extends PHttpMessage {
    protected String method;
    protected String url;

    public PHttpRequest(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature({
            @Arg(value = "method"),
            @Arg(value = "url"),
    })
    public Memory __construct(Environment env, Memory... args) throws Throwable {
        env.invokeMethod(this, "setMethod", args[0]);
        env.invokeMethod(this, "setUrl", args[1]);

        return Memory.NULL;
    }

    @Signature
    public String getMethod() {
        return method;
    }

    @Signature
    public void setMethod(String method) {
        this.method = method;
    }

    @Signature
    public String getUrl() {
        return url;
    }

    @Signature
    public void setUrl(String url) {
        this.url = url;
    }
}
