package org.develnext.jphp.http.classes;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.memory.ObjectMemory;
import php.runtime.reflection.ClassEntity;

import java.util.Map;

import static php.runtime.annotation.Reflection.*;

@Name("php\\net\\HttpClient")
public class WrapHttpClient extends BaseObject {
    protected Map<String, String> headers;

    public WrapHttpClient(Environment env) {
        super(env);
    }

    public WrapHttpClient(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    protected HttpRequestBase makeHttpRequest(HttpRequestBase httpRequestBase) {
        if (headers != null) {
            for (Map.Entry<String, String> el : headers.entrySet()) {
                httpRequestBase.setHeader(el.getKey(), el.getValue());
            }
        }

        return httpRequestBase;
    }

    @Signature(@Arg("url"))
    public Memory get(Environment env, Memory... args) {
        WrapHttpRequest request = new WrapHttpRequest(env, this);
        request.setHttpRequest(makeHttpRequest(new HttpGet(args[0].toString())));

        return new ObjectMemory(request);
    }
}
