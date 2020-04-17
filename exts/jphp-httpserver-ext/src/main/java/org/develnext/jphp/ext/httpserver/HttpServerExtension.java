package org.develnext.jphp.ext.httpserver;

import org.develnext.jphp.ext.httpserver.classes.*;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.common.WebSocketSession;
import php.runtime.env.CompileScope;
import php.runtime.ext.support.Extension;
import php.runtime.memory.support.MemoryOperation;

import javax.servlet.http.Part;

class NoLogging implements Logger {
    @Override public String getName() { return "no"; }
    @Override public void warn(String msg, Object... args) { }
    @Override public void warn(Throwable thrown) {
        thrown.printStackTrace();
    }

    @Override public void warn(String msg, Throwable thrown) {
        System.err.println(msg);
        thrown.printStackTrace();
    }

    @Override public void info(String msg, Object... args) { }
    @Override public void info(Throwable thrown) { }
    @Override public void info(String msg, Throwable thrown) { }
    @Override public boolean isDebugEnabled() { return false; }
    @Override public void setDebugEnabled(boolean enabled) { }
    @Override public void debug(String msg, Object... args) { }
    @Override public void debug(String msg, long value) {}
    @Override public void debug(Throwable thrown) { }
    @Override public void debug(String msg, Throwable thrown) { }
    @Override public Logger getLogger(String name) { return this; }
    @Override public void ignore(Throwable ignored) { }
}

public class HttpServerExtension extends Extension {
    public static final String NS = "php\\http";

    @Override
    public Status getStatus() {
        return Status.BETA;
    }

    @Override
    public String[] getPackageNames() {
        return new String[] { "http" };
    }

    @Override
    public void onRegister(CompileScope scope) {
        registerWrapperClass(scope, Session.class, PWebSocketSession.class);
        registerWrapperClass(scope, Part.class, PHttpPart.class);
        MemoryOperation.registerWrapper(WebSocketSession.class, PWebSocketSession.class);

        registerClass(scope, PHttpServer.class);
        registerClass(scope, PHttpServerRequest.class);
        registerClass(scope, PHttpServerResponse.class);
        registerClass(scope, PHttpAbstractHandler.class);
        registerClass(scope, PHttpRouteFilter.class);
        registerClass(scope, PHttpRouteHandler.class);
        registerClass(scope, PHttpRedirectHandler.class);
        registerClass(scope, PHttpDownloadFileHandler.class);
        registerClass(scope, PHttpResourceHandler.class);
        registerClass(scope, PHttpCORSFilter.class);
        registerClass(scope, PHttpGzipFilter.class);

        Log.setLog(new NoLogging());
    }
}
