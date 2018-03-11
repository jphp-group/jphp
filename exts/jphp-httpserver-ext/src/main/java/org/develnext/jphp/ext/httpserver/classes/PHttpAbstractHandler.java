package org.develnext.jphp.ext.httpserver.classes;

import org.develnext.jphp.ext.httpserver.HttpServerExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.reflection.ClassEntity;

@Name("HttpAbstractHandler")
@Namespace(HttpServerExtension.NS)
abstract public class PHttpAbstractHandler extends BaseObject {
    public PHttpAbstractHandler(Environment env) {
        super(env);
    }

    public PHttpAbstractHandler(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }
}
