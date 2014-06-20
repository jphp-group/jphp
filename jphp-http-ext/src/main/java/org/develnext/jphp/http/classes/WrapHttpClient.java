package org.develnext.jphp.http.classes;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.lang.BaseObject;
import php.runtime.lang.ForeachIterator;
import php.runtime.lang.support.ICloneableObject;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.reflection.ClassEntity;

import java.util.LinkedHashMap;
import java.util.Map;

import static php.runtime.annotation.Reflection.*;

@Name("php\\net\\HttpClient")
public class WrapHttpClient extends BaseObject implements ICloneableObject<WrapHttpClient> {
    protected RequestConfig.Builder configBuilder = RequestConfig.copy(RequestConfig.DEFAULT);
    protected Map<String, String> headers = new LinkedHashMap<String, String>();

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
        httpRequestBase.setConfig(configBuilder.build());
        return httpRequestBase;
    }

    @Signature
    public Memory getHeaders(Environment env, Memory... args) {
        return new ArrayMemory(headers).toConstant();
    }

    @Signature(@Arg(value = "headers", type = HintType.ARRAY))
    public Memory setHeaders(Environment env, Memory... args) {
        ForeachIterator iterator = args[0].getNewIterator(env);
        headers.clear();
        while (iterator.next()) {
            headers.put(iterator.getMemoryKey().toString(), iterator.getValue().toString());
        }

        return new ObjectMemory(this);
    }

    @Signature(@Arg("millis"))
    public Memory setConnectTimeout(Environment env, Memory... args) {
        configBuilder.setConnectTimeout(args[0].toInteger());
        return new ObjectMemory(this);
    }

    @Signature(@Arg("value"))
    public Memory setMaxRedirects(Environment env, Memory... args) {
        configBuilder.setMaxRedirects(args[0].toInteger());
        return new ObjectMemory(this);
    }

    @Signature(@Arg("value"))
    public Memory setSocketTimeout(Environment env, Memory... args) {
        configBuilder.setSocketTimeout(args[0].toInteger());
        return new ObjectMemory(this);
    }

    @Signature(@Arg("value"))
    public Memory setRedirectsEnabled(Environment env, Memory... args) {
        configBuilder.setRedirectsEnabled(args[0].toBoolean());
        return new ObjectMemory(this);
    }

    @Signature({
            @Arg("hostname"),
            @Arg(value = "port", optional = @Optional("-1")),
            @Arg(value = "scheme", optional = @Optional("http"))
    })
    public Memory setProxy(Environment env, Memory... args) {
        configBuilder.setProxy(new HttpHost(args[0].toString(), args[1].toInteger(), args[2].toString()));
        return new ObjectMemory(this);
    }

    @Signature(@Arg("url"))
    public Memory get(Environment env, Memory... args) {
        WrapHttpRequest request = new WrapHttpRequest(env, this);
        request.setHttpRequest(makeHttpRequest(new HttpGet(args[0].toString())));

        return new ObjectMemory(request);
    }

    @Signature(@Arg("url"))
    public Memory post(Environment env, Memory... args) {
        WrapHttpRequest request = new WrapHttpRequest(env, this);
        request.setHttpRequest(makeHttpRequest(new HttpPost(args[0].toString())));

        return new ObjectMemory(request);
    }

    @Signature(@Arg("url"))
    public Memory put(Environment env, Memory... args) {
        WrapHttpRequest request = new WrapHttpRequest(env, this);
        request.setHttpRequest(makeHttpRequest(new HttpPut(args[0].toString())));

        return new ObjectMemory(request);
    }

    @Signature(@Arg("url"))
    public Memory delete(Environment env, Memory... args) {
        WrapHttpRequest request = new WrapHttpRequest(env, this);

        request.setHttpRequest(makeHttpRequest(new HttpDelete(args[0].toString())));
        return new ObjectMemory(request);
    }

    @Signature(@Arg("url"))
    public Memory head(Environment env, Memory... args) {
        WrapHttpRequest request = new WrapHttpRequest(env, this);

        request.setHttpRequest(makeHttpRequest(new HttpHead(args[0].toString())));
        return new ObjectMemory(request);
    }

    @Signature(@Arg("url"))
    public Memory patch(Environment env, Memory... args) {
        WrapHttpRequest request = new WrapHttpRequest(env, this);

        request.setHttpRequest(makeHttpRequest(new HttpPatch(args[0].toString())));
        return new ObjectMemory(request);
    }


    @Signature(@Arg("url"))
    public Memory options(Environment env, Memory... args) {
        WrapHttpRequest request = new WrapHttpRequest(env, this);

        request.setHttpRequest(makeHttpRequest(new HttpOptions(args[0].toString())));
        return new ObjectMemory(request);
    }

    @Override
    public WrapHttpClient __clone(Environment env, TraceInfo trace) {
        WrapHttpClient r = new WrapHttpClient(env);
        r.configBuilder = RequestConfig.copy(configBuilder.build());
        r.headers = new LinkedHashMap<String, String>(headers);

        return r;
    }
}
