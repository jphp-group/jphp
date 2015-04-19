package org.develnext.jphp.ext.webserver;

import org.develnext.jphp.ext.webserver.classes.PWebServer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import php.runtime.env.CompileScope;
import php.runtime.ext.support.Extension;

public class WebServerExtension extends Extension {
    public static final String NS = "php\\webserver";

    @Override
    public Status getStatus() {
        return Status.EXPERIMENTAL;
    }

    @Override
    public void onRegister(CompileScope scope) {
        registerClass(scope, PWebServer.class);
    }
}
