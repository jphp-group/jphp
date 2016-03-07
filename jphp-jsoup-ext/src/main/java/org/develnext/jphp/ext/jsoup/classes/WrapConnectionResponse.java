package org.develnext.jphp.ext.jsoup.classes;

import org.develnext.jphp.ext.jsoup.JsoupExtension;
import org.jsoup.Connection;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Abstract;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.annotation.Reflection.WrapInterface;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

import java.util.Map;

import static php.runtime.annotation.Reflection.Name;

@Abstract
@Name("ConnectionResponse")
@Namespace(JsoupExtension.NS)
@WrapInterface(Connection.Response.class)
public class WrapConnectionResponse extends BaseWrapper<Connection.Response> {
    public WrapConnectionResponse(Environment env, Connection.Response object) {
        super(env, object);
    }

    public WrapConnectionResponse(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public Map<String, String> headers() {
        return getWrappedObject().headers();
    }

    @Signature
    public Map<String, String> cookies() {
        return getWrappedObject().cookies();
    }
}
