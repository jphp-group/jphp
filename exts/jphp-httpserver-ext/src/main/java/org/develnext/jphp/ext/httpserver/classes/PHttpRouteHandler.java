package org.develnext.jphp.ext.httpserver.classes;

import org.develnext.jphp.ext.httpserver.HttpServerExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Name("HttpRouteHandler")
@Namespace(HttpServerExtension.NS)
public class PHttpRouteHandler extends PHttpRouteFilter {
    public PHttpRouteHandler(Environment env) {
        super(env);
    }

    public PHttpRouteHandler(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    @Signature
    public boolean __invoke(PHttpServerRequest request, PHttpServerResponse response) throws Throwable {
        boolean invoke = super.__invoke(request, response);

        if (invoke) {
            request.end();
        }

        return invoke;
    }
}
