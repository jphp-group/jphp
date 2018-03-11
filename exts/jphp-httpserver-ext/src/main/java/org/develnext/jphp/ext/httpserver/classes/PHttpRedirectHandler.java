package org.develnext.jphp.ext.httpserver.classes;


import org.develnext.jphp.ext.httpserver.HttpServerExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.memory.ArrayMemory;
import php.runtime.reflection.ClassEntity;

import java.io.IOException;

@Reflection.Name("HttpRedirectHandler")
@Reflection.Namespace(HttpServerExtension.NS)
public class PHttpRedirectHandler extends PHttpAbstractHandler {
    private String url;
    private boolean permanently;

    public PHttpRedirectHandler(Environment env) {
        super(env);
    }

    public PHttpRedirectHandler(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct(String url) {
        __construct(url, false);
    }

    @Signature
    public void __construct(String url, boolean permanently) {
        reset(url, permanently);
    }

    @Signature
    public Memory __debugInfo() {
        ArrayMemory arr = ArrayMemory.createHashed();
        arr.refOfIndex("*url").assign(url);
        arr.refOfIndex("*permanently").assign(permanently);
        return arr;
    }

    @Signature
    public String url() {
        return url;
    }

    @Signature
    public boolean permanently() {
        return permanently;
    }

    @Signature
    public void reset(String url) {
        reset(url, false);
    }

    @Signature
    synchronized public void reset(String url, boolean permanently) {
        this.url = url;
        this.permanently = permanently;
    }

    @Signature
    public void __invoke(PHttpServerRequest request, PHttpServerResponse response) throws IOException {
        String method = request.method().toUpperCase();

        if (permanently) {
            response.status(301);
        } else {
            switch (method) {
                case "POST":
                case "PUT":
                    response.status(303);
                    break;

                default:
                    response.status(302);
                    break;
            }
        }

        response.getResponse().sendRedirect(response.encodeRedirectUrl(url));
        request.end();
    }
}
