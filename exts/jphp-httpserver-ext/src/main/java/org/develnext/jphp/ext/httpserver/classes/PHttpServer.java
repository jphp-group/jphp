package org.develnext.jphp.ext.httpserver.classes;

import javax.servlet.RequestDispatcher;
import org.develnext.jphp.ext.httpserver.HttpServerExtension;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.*;
import org.eclipse.jetty.server.session.DefaultSessionIdManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import php.runtime.Memory;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Nullable;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseObject;
import php.runtime.memory.*;
import php.runtime.reflection.ClassEntity;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@Name("HttpServer")
@Namespace(HttpServerExtension.NS)
public class PHttpServer extends BaseObject {
    private Server server;
    private HandlerList handlers = new HandlerList();
    private HandlerList filters = new HandlerList();
    private SessionIdManager idmanager;
    private QueuedThreadPool threadPool;

    public PHttpServer(Environment env, Server server) {
        super(env);
        this.server = server;
    }

    public PHttpServer(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        threadPool = new QueuedThreadPool();
        server = new Server(threadPool);
        initSessionManager();
    }

    @Signature
    public void __construct(int port) {
        __construct(port, null);
    }

    private void initSessionManager() {
        idmanager = new DefaultSessionIdManager(server);
        server.setSessionIdManager(idmanager);

        SessionHandler sessions = new SessionHandler();
        sessions.setSessionIdManager(idmanager);
        filters.addHandler(sessions);
    }

    @Signature
    public void __construct(int port, String host) {
        threadPool = new QueuedThreadPool();
        server = new Server(threadPool);

        if (host == null || host.isEmpty()) {
            listen(LongMemory.valueOf(port));
        } else {
            listen(StringMemory.valueOf(host + ":" + port));
        }

        initSessionManager();
    }

    @Signature
    public void minThreads(int minThreads) {
        threadPool.setMinThreads(minThreads);
    }

    @Signature
    public int minThreads() {
        return threadPool.getMinThreads();
    }

    @Signature
    public void maxThreads(int maxThreads) {
        threadPool.setMaxThreads(maxThreads);
    }

    @Signature
    public int maxThreads() {
        return threadPool.getMaxThreads();
    }

    @Signature
    public void threadIdleTimeout(int timeout) {
        threadPool.setIdleTimeout(timeout);
    }

    @Signature
    public int threadIdleTimeout() {
        return threadPool.getIdleTimeout();
    }

    @Signature
    public boolean stopAtShutdown() {
        return server.getStopAtShutdown();
    }

    @Signature
    public void stopAtShutdown(boolean val) {
        server.setStopAtShutdown(val);
    }

    @Signature
    public void clearHandlers() {
        handlers.setHandlers(new Handler[0]);
    }

    @Signature
    public void addWebSocket(Environment env, String path, ArrayMemory _handlers) {
        WebSocketParam param = _handlers.toBean(env, WebSocketParam.class);

        ContextHandler contextHandler = new ContextHandler(path);
        contextHandler.setHandler(new WebSocketHandler() {
            @Override
            public void configure(WebSocketServletFactory factory) {
                factory.setCreator(new WebSocketCreator() {
                    @Override
                    public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
                        return new WebSocket(env, param);
                    }
                });
            }
        });

        handlers.addHandler(contextHandler);
    }

    @Signature
    public void addHandler(final Invoker invoker) {
        handlers.addHandler(new InvokeHandler(invoker));
    }

    @Signature
    public void addFilter(final Invoker invoker) {
        filters.addHandler(new InvokeHandler(invoker));
    }

    @Signature
    public Memory handlers() {
        ArrayMemory result = ArrayMemory.createListed(handlers.getHandlers().length);

        for (Handler handler : handlers.getHandlers()) {
            if (handler instanceof InvokeHandler) {
                Invoker invoker = ((InvokeHandler) handler).getInvoker();
                result.add(invoker.getMemory().toImmutable());
            }
        }

        return result.toImmutable();
    }

    @Signature
    public Memory filters() {
        ArrayMemory result = ArrayMemory.createListed(filters.getHandlers().length);

        for (Handler handler : filters.getHandlers()) {
            if (handler instanceof InvokeHandler) {
                Invoker invoker = ((InvokeHandler) handler).getInvoker();
                result.add(invoker.getMemory().toImmutable());
            }
        }

        return result.toImmutable();
    }

    @Signature
    public PHttpRouteFilter filtrate(Environment env, Memory methods, String path, Memory invoker) {
        PHttpRouteFilter routeHandler = new PHttpRouteFilter(env);
        routeHandler.reset(env, methods, path, invoker);

        filters.addHandler(new InvokeHandler(Invoker.create(env, ObjectMemory.valueOf(routeHandler))));
        return routeHandler;
    }

    @Signature
    public PHttpRouteHandler route(Environment env, Memory methods, String path, Memory invoker) {
        PHttpRouteHandler routeHandler = new PHttpRouteHandler(env);
        routeHandler.reset(env, methods, path, invoker);

        addHandler(Invoker.create(env, ObjectMemory.valueOf(routeHandler)));
        return routeHandler;
    }

    @Signature
    public PHttpRouteHandler any(Environment env, String path, Memory invoker) {
        return route(env, StringMemory.valueOf("*"), path, invoker);
    }

    @Signature
    public PHttpRouteHandler get(Environment env, String path, Memory invoker) {
        return route(env, StringMemory.valueOf("GET"), path, invoker);
    }

    @Signature
    public PHttpRouteHandler post(Environment env, String path, Memory invoker) {
        return route(env, StringMemory.valueOf("POST"), path, invoker);
    }

    @Signature
    public PHttpRouteHandler put(Environment env, String path, Memory invoker) {
        return route(env, StringMemory.valueOf("PUT"), path, invoker);
    }

    @Signature
    public PHttpRouteHandler delete(Environment env, String path, Memory invoker) {
        return route(env, StringMemory.valueOf("DELETE"), path, invoker);
    }

    @Signature
    public PHttpRouteHandler options(Environment env, String path, Memory invoker) {
        return route(env, StringMemory.valueOf("OPTIONS"), path, invoker);
    }

    @Signature
    public PHttpRouteHandler patch(Environment env, String path, Memory invoker) {
        return route(env, StringMemory.valueOf("PATCH"), path, invoker);
    }

    @Signature
    public PHttpRouteHandler head(Environment env, String path, Memory invoker) {
        return route(env, StringMemory.valueOf("HEAD"), path, invoker);
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
            connector.setName("0.0.0.0:" + value.toInteger());
            connector.setPort(value.toInteger());
        } else {
            String[] strings = value.toString().split("\\:");

            if (strings.length < 2) {
                throw new IllegalArgumentException("Invalid listen value: " + value);
            }

            connector.setHost(strings[0]);
            connector.setPort(Integer.parseInt(strings[1]));
            connector.setName(strings[0] + ":" + strings[1]);
        }

        server.addConnector(connector);
    }

    @Signature
    public boolean unlisten(Environment env, String value) {
        if (server.isRunning()) {
            env.exception("Unable to unlisten() for running server");
        }

        String host = "0.0.0.0";
        String port = "80";

        if (value.contains(":")) {
            String[] strings = value.split("\\:");
            host = strings[0];

            if (strings.length > 1) {
                port = strings[1];
            }
        }

        for (Connector connector : server.getConnectors()) {
            if (connector.getName().equals(host + ":" + port)) {
                server.removeConnector(connector);
                return true;
            }
        }

        return false;
    }

    @Signature
    public Memory connectors() {
        Connector[] connectors = server.getConnectors();
        ArrayMemory arrayMemory = ArrayMemory.createListed(connectors.length);

        for (Connector connector : connectors) {
            arrayMemory.add(connector.getName());
        }

        return arrayMemory.toImmutable();
    }

    @Signature
    public void runInBackground() throws Exception {
        server.setErrorHandler(new ErrorHandler() {
            @Override
            public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
                super.handle(target, baseRequest, request, response);
            }
        });

        HandlerList handlerList = new HandlerList();

        for (Handler handler : filters.getHandlers()) {
            handlerList.addHandler(handler);
        }

        for (Handler handler : handlers.getHandlers()) {
            handlerList.addHandler(handler);
        }

        server.setHandler(handlerList);
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

    @Signature
    public boolean isRunning() {
        return server.isRunning();
    }

    @Signature
    public boolean isFailed() {
        return server.isFailed();
    }

    @Signature
    public boolean isStarting() {
        return server.isStarting();
    }

    @Signature
    public boolean isStopping() {
        return server.isStopping();
    }

    @Signature
    public boolean isStopped() {
        return server.isStopped();
    }

    @Signature
    public void setRequestLogHandler(Environment env, @Nullable Invoker invoker) {
        if (invoker == null) {
            server.setRequestLog(null);
        } else {
            server.setRequestLog((request, response) -> {
                invoker.callAny(new PHttpServerRequest(env, request), new PHttpServerResponse(env, response));
            });
        }
    }

    @Signature
    public void setErrorHandler(Environment env, @Nullable Invoker invoker) {
        if (invoker == null) {
            server.setErrorHandler(null);
        } else {
            ErrorHandler errorHandler = new ErrorHandler();

            server.setErrorHandler(new ErrorHandler() {
                @Override
                public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
                    Throwable th = (Throwable)request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
                    invoker.callAny(th, new PHttpServerRequest(env, Request.getBaseRequest(request)), new PHttpServerResponse(env, response));
                }
            });
        }
    }

    public static class InvokeHandler extends AbstractHandler {
        private final Invoker invoker;

        public InvokeHandler(Invoker invoker) {
            this.invoker = invoker;
        }

        public Invoker getInvoker() {
            return invoker;
        }

        @Override
        public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
            try {
                invoker.callAny(new PHttpServerRequest(invoker.getEnvironment(), baseRequest), new PHttpServerResponse(invoker.getEnvironment(), response));
            } catch (Throwable e) {
                Environment.catchThrowable(e, invoker.getEnvironment());
            }
        }
    }

    public static class WebSocketParam {
        private Invoker onConnect;
        private Invoker onClose;
        private Invoker onError;
        private Invoker onMessage;
        private Invoker onBinaryMessage;

        public Invoker getOnConnect() {
            return onConnect;
        }

        public void setOnConnect(Invoker onConnect) {
            this.onConnect = onConnect;
        }

        public Invoker getOnClose() {
            return onClose;
        }

        public void setOnClose(Invoker onClose) {
            this.onClose = onClose;
        }

        public Invoker getOnError() {
            return onError;
        }

        public void setOnError(Invoker onError) {
            this.onError = onError;
        }

        public Invoker getOnMessage() {
            return onMessage;
        }

        public void setOnMessage(Invoker onMessage) {
            this.onMessage = onMessage;
        }

        public Invoker getOnBinaryMessage() {
            return onBinaryMessage;
        }

        public void setOnBinaryMessage(Invoker onBinaryMessage) {
            this.onBinaryMessage = onBinaryMessage;
        }
    }

    @org.eclipse.jetty.websocket.api.annotations.WebSocket
    public static class WebSocket {
        private final Environment env;
        private final WebSocketParam param;

        public WebSocket(Environment env, WebSocketParam param) {
            this.env = env;
            this.param = param;
        }

        @OnWebSocketConnect
        public void onConnect(Session session) {
            if (param.onConnect != null) {
                param.onConnect.callAny(session);
            }
        }

        @OnWebSocketClose
        public void onClose(Session session, int statusCode, String reason) {
            if (param.onClose != null) {
                param.onClose.callAny(session, statusCode, reason);
            }
        }

        @OnWebSocketError
        public void onError(Session session, Throwable throwable) {
            if (param.onError != null) {
                param.onError.callAny(session, throwable);
            }
        }

        @OnWebSocketMessage
        public void onMessage(Session session, String text) {
            if (param.onMessage != null) {
                param.onMessage.callAny(session, text);
            }
        }

        @OnWebSocketMessage
        public void onBinaryMessage(Session session, InputStream stream) {
            if (param.onBinaryMessage != null) {
                param.onBinaryMessage.callAny(session, stream);
            }
        }
    }
}
