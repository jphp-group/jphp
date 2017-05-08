package org.develnext.jphp.ext.httpserver;

import org.develnext.jphp.ext.httpserver.classes.PHttpServer;
import org.develnext.jphp.ext.httpserver.classes.PHttpServerRequest;
import org.develnext.jphp.ext.httpserver.classes.PHttpServerResponse;
import php.runtime.env.CompileScope;
import php.runtime.ext.support.Extension;

public class HttpServerExtension extends Extension {
    public static final String NS = "php\\http";

    @Override
    public Status getStatus() {
        return Status.EXPERIMENTAL;
    }

    @Override
    public String[] getPackageNames() {
        return new String[] { "http" };
    }

    @Override
    public void onRegister(CompileScope scope) {
        registerClass(scope, PHttpServer.class);
        registerClass(scope, PHttpServerRequest.class);
        registerClass(scope, PHttpServerResponse.class);
    }
}
