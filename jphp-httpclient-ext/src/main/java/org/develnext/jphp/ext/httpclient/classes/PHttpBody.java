package org.develnext.jphp.ext.httpclient.classes;

import org.develnext.jphp.ext.httpclient.HttpClientExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Arg;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.ext.net.WrapURLConnection;
import php.runtime.lang.BaseObject;
import php.runtime.reflection.ClassEntity;

import java.io.IOException;

@Name("PHttpBody")
@Namespace(HttpClientExtension.NS)
abstract public class PHttpBody extends BaseObject {
    public PHttpBody(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public PHttpBody(Environment env) {
        super(env);
    }

    @Signature(@Arg(value = "connection", nativeType = WrapURLConnection.class))
    abstract public Memory apply(Environment env, Memory... args) throws IOException;
}
