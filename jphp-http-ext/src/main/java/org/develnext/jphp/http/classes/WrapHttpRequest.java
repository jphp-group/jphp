package org.develnext.jphp.http.classes;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.develnext.jphp.http.classes.entity.WrapHttpEntity;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.memory.ObjectMemory;
import php.runtime.reflection.ClassEntity;

import java.io.IOException;

import static php.runtime.annotation.Reflection.*;

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

    @Signature(
            @Arg(value = "entity", nativeType = WrapHttpEntity.class, optional = @Optional("null"))
    )
    public Memory execute(Environment env, Memory... args) throws IOException {
        if (!args[0].isNull()) {
            ((HttpEntityEnclosingRequestBase)httpRequest).setEntity(
                    args[0].toObject(WrapHttpEntity.class).getEntity()
            );
        }

        CloseableHttpResponse response = httpClient.execute(httpRequest);
        return new ObjectMemory(new WrapHttpResponse(env, response));
    }
}
