package org.develnext.jphp.ext.webserver;

import org.develnext.jphp.ext.webserver.classes.PWebRequest;
import org.develnext.jphp.ext.webserver.classes.PWebServer;
import php.runtime.env.CompileScope;
import php.runtime.ext.support.Extension;

import javax.servlet.http.HttpServletRequest;

public class WebServerExtension extends Extension {
    public static final String NS = "php\\webserver";

    @Override
    public Status getStatus() {
        return Status.EXPERIMENTAL;
    }

    @Override
    public void onRegister(CompileScope scope) {
        registerClass(scope, PWebServer.class);
        registerWrapperClass(scope, HttpServletRequest.class, PWebRequest.class);
    }
}
