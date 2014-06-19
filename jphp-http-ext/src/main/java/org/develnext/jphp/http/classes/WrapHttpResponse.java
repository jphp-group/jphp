package org.develnext.jphp.http.classes;

import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.develnext.jphp.http.classes.entity.WrapHttpEntity;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.reflection.ClassEntity;

import static php.runtime.annotation.Reflection.*;

@Name("php\\net\\HttpResponse")
public class WrapHttpResponse extends BaseObject {
    protected CloseableHttpResponse httpResponse;

    public WrapHttpResponse(Environment env, CloseableHttpResponse httpResponse) {
        super(env);
        this.httpResponse = httpResponse;
    }

    public WrapHttpResponse(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public Memory getHeaders(Environment env, Memory... args) {
        ArrayMemory r = new ArrayMemory();
        for (Header header : httpResponse.getAllHeaders()) {
            r.refOfIndex(header.getName()).assign(header.getValue());
        }
        return r.toConstant();
    }

    @Signature
    public Memory getStatusCode(Environment env, Memory... args) {
        return LongMemory.valueOf(httpResponse.getStatusLine().getStatusCode());
    }

    @Signature(@Arg(value = "entity", nativeType = WrapHttpEntity.class, optional = @Optional("null")))
    public Memory setEntity(Environment env, Memory... args) {
        httpResponse.setEntity(args[0].isNull() ? null : args[0].toObject(WrapHttpEntity.class).getEntity());
        return Memory.NULL;
    }

    @Signature
    public Memory getEntity(Environment env, Memory... args) {
        return new ObjectMemory(new WrapHttpEntity(env, httpResponse.getEntity()));
    }
}
