package org.develnext.jphp.ext.jsoup.classes;

import org.develnext.jphp.ext.jsoup.JsoupExtension;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.memory.BinaryMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

import java.io.IOException;

import static php.runtime.annotation.Reflection.Name;
import static php.runtime.annotation.Reflection.Signature;

@Name(JsoupExtension.NAMESPACE + "ConnectionResponse")
@Reflection.WrapInterface(Connection.Response.class)
public class WrapConnectionResponse extends BaseWrapper<Connection.Response> {
    public WrapConnectionResponse(Environment env, Connection.Response object) {
        super(env, object);
    }

    public WrapConnectionResponse(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }
}
