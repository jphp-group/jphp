package org.develnext.jphp.http.classes;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.memory.ObjectMemory;
import php.runtime.reflection.ClassEntity;

import java.io.IOException;

import static php.runtime.annotation.Reflection.Name;
import static php.runtime.annotation.Reflection.Signature;

@Name("php\\net\\HttpRequest")
public class WrapHttpRequest extends BaseObject {
    protected WrapHttpClient client;
    protected HttpRequestBase httpRequest;
    protected CloseableHttpClient httpClient;

    public WrapHttpRequest(Environment env, WrapHttpClient client) {
        super(env);
        this.client = client;
        this.httpClient = HttpClients.createDefault();
    }

    public WrapHttpRequest(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    private Memory __construct(Environment env, Memory... args) {
        return Memory.NULL;
    }

    void setHttpRequest(HttpRequestBase httpRequest) {
        this.httpRequest = httpRequest;
    }

    @Signature
    public Memory execute(Environment env, Memory... args) throws IOException {
        CloseableHttpResponse response = httpClient.execute(httpRequest);
        return new ObjectMemory(new WrapHttpResponse(env, response));
    }
}
