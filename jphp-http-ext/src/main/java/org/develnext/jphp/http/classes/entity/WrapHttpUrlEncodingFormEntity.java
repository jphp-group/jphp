package org.develnext.jphp.http.classes.entity;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.lang.ForeachIterator;
import php.runtime.reflection.ClassEntity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static php.runtime.annotation.Reflection.*;

@Name("php\\net\\HttpUrlEncodingFormEntity")
public class WrapHttpUrlEncodingFormEntity extends WrapHttpEntity {
    public WrapHttpUrlEncodingFormEntity(Environment env, HttpEntity entity) {
        super(env, entity);
    }

    public WrapHttpUrlEncodingFormEntity(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature({
            @Arg(value = "args", type = HintType.ARRAY),
            @Arg(value = "encoding", optional = @Optional("null"))
    })
    public Memory __construct(Environment env, Memory... args) throws UnsupportedEncodingException {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();

        ForeachIterator iterator = args[0].getNewIterator(env);
        while (iterator.next()) {
            pairs.add(new BasicNameValuePair(iterator.getKey().toString(), iterator.getValue().toString()));
        }


        this.entity = new UrlEncodedFormEntity(pairs,
                args[1].isNull() ? env.getDefaultCharset().name() : args[1].toString()
        );
        return Memory.NULL;
    }
}
