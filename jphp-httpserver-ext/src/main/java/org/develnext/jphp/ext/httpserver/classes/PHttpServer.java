package org.develnext.jphp.ext.httpserver.classes;

import org.develnext.jphp.ext.httpserver.HttpServerExtension;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.session.DefaultSessionIdManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import php.runtime.Memory;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseObject;
import php.runtime.memory.ArrayMemory;
import php.runtime.reflection.ClassEntity;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetSocketAddress;

@Name("HttpServer")
@Namespace(HttpServerExtension.NS)
public class PHttpServer extends BaseObject {
    private Server server;
    private HandlerList handlers = new HandlerList();
    private SessionIdManager idmanager;

    public PHttpServer(Environment env, Server server) {
        super(env);
        this.server = server;
    }

    public PHttpServer(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        server = new Server();

        initSessionManager();
    }

    @Signature
    public void __construct(int port) {
        server = new Server(port);

        initSessionManager();
    }

    private void initSessionManager() {
        idmanager = new DefaultSessionIdManager(server);
        server.setSessionIdManager(idmanager);

        SessionHandler sessions = new SessionHandler();
        sessions.setSessionIdManager(idmanager);
        handlers.addHandler(sessions);
    }

    @Signature
    public void __construct(int port, String host) {
        server = new Server(new InetSocketAddress(host, port));
    }

    @Signature
    public void addHandler(final Environment env, final Invoker invoker) {
        handlers.addHandler(new AbstractHandler() {
            @Override
            public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
                invoker.callAny(new PHttpServerRequest(env, baseRequest), new PHttpServerResponse(env, response));
            }
        });
    }

    @Signature
    public void addResourceHandler(ArrayMemory h) {
        ResourceHandler handler = new ResourceHandler();

        if (!h.containsKey("base")) {
            throw new IllegalArgumentException("Resource handler must contain 'base' value");
        }

        handler.setResourceBase(h.valueOfIndex("base").toString());

        if (h.containsKey("cacheControl")) {
            handler.setCacheControl(h.valueOfIndex("cacheControl").toString());
        }

        if (h.containsKey("acceptRanges")) {
            handler.setAcceptRanges(h.valueOfIndex("acceptRanges").toBoolean());
        }

        if (h.containsKey("dirAllowed")) {
            handler.setDirAllowed(h.valueOfIndex("dirAllowed").toBoolean());
        }

        if (h.containsKey("dirsListed")) {
            handler.setDirectoriesListed(h.valueOfIndex("dirsListed").toBoolean());
        }

        if (h.containsKey("etags")) {
            handler.setEtags(h.valueOfIndex("etags").toBoolean());
        }

        if (h.containsKey("pathInfoOnly")) {
            handler.setPathInfoOnly(h.valueOfIndex("pathInfoOnly").toBoolean());
        }

        if (h.containsKey("welcomeFile")) {
            handler.setWelcomeFiles(new String[] { h.valueOfIndex("welcomeFile").toString() });
        }

        handlers.addHandler(handler);
    }

    @Signature
    public void listen(Memory value) {
        listen(value, null);
    }

    @Signature
    public void listen(Memory value, ArrayMemory sslSettings) {
        ServerConnector connector;

        if (sslSettings != null) {
            SslContextFactory contextFactory = new SslContextFactory();

            // key store
            if (sslSettings.containsKey("keyStorePath"))
                contextFactory.setKeyStorePath(sslSettings.valueOfIndex("keyStorePath").toString());

            if (sslSettings.containsKey("keyStorePassword"))
                contextFactory.setKeyStoreType(sslSettings.valueOfIndex("keyStorePassword").toString());

            if (sslSettings.containsKey("keyStoreType"))
                contextFactory.setKeyStoreType(sslSettings.valueOfIndex("keyStoreType").toString());

            if (sslSettings.containsKey("keyStoreProvider"))
                contextFactory.setKeyStoreProvider(sslSettings.valueOfIndex("keyStoreProvider").toString());

            // trust store
            if (sslSettings.containsKey("trustStorePath"))
                contextFactory.setTrustStorePath(sslSettings.valueOfIndex("trustStorePath").toString());

            if (sslSettings.containsKey("trustStorePassword"))
                contextFactory.setTrustStoreType(sslSettings.valueOfIndex("trustStorePassword").toString());

            if (sslSettings.containsKey("trustStoreType"))
                contextFactory.setTrustStoreType(sslSettings.valueOfIndex("trustStoreType").toString());

            if (sslSettings.containsKey("trustStoreProvider"))
                contextFactory.setTrustStoreProvider(sslSettings.valueOfIndex("trustStoreProvider").toString());

            if (sslSettings.containsKey("trustAll"))
                contextFactory.setTrustAll(sslSettings.valueOfIndex("trustAll").toBoolean());

            if (sslSettings.containsKey("trustManagerFactoryAlgorithm"))
                contextFactory.setTrustManagerFactoryAlgorithm(sslSettings.valueOfIndex("trustManagerFactoryAlgorithm").toString());

            // key manager
            if (sslSettings.containsKey("keyManagerFactoryAlgorithm"))
                contextFactory.setKeyManagerFactoryAlgorithm(sslSettings.valueOfIndex("keyManagerFactoryAlgorithm").toString());

            if (sslSettings.containsKey("keyManagerPassword"))
                contextFactory.setKeyManagerPassword(sslSettings.valueOfIndex("keyManagerPassword").toString());

            // other
            if (sslSettings.containsKey("certAlias"))
                contextFactory.setCertAlias(sslSettings.valueOfIndex("certAlias").toString());

            if (sslSettings.containsKey("protocol"))
                contextFactory.setProtocol(sslSettings.valueOfIndex("protocol").toString());

            if (sslSettings.containsKey("provider"))
                contextFactory.setProvider(sslSettings.valueOfIndex("provider").toString());

            if (sslSettings.containsKey("validateCerts"))
                contextFactory.setValidateCerts(sslSettings.valueOfIndex("validateCerts").toBoolean());

            connector = new ServerConnector(server, contextFactory);
        } else {
            connector = new ServerConnector(server);
        }

        if (value.isNumber()) {
            connector.setPort(value.toInteger());
        } else {
            String[] strings = value.toString().split("\\:");

            if (strings.length < 2) {
                throw new IllegalArgumentException("Invalid listen value: " + value);
            }

            connector.setHost(strings[0]);
            connector.setPort(Integer.parseInt(strings[1]));
        }

        server.addConnector(connector);
    }

    @Signature
    public void runInBackground() throws Exception {
        server.setHandler(handlers);
        server.start();
    }

    @Signature
    public void run() throws Exception {
        runInBackground();
        server.join();
    }

    @Signature
    public void shutdown() throws Exception {
        server.stop();
    }
}
