package org.develnext.jphp.http.classes;

import org.apache.http.client.methods.*;
import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.lang.ForeachIterator;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.reflection.ClassEntity;

import java.util.LinkedHashMap;
import java.util.Map;

import static php.runtime.annotation.Reflection.*;

@Name("php\\net\\HttpClient")
public class WrapHttpClient extends BaseObject {
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

        return Memory.NULL;
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
}
