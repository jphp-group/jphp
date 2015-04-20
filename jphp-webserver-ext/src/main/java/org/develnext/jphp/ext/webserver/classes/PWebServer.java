package org.develnext.jphp.ext.webserver.classes;

import org.develnext.jphp.ext.webserver.WebServerConfig;
import org.develnext.jphp.ext.webserver.WebServerController;
import org.develnext.jphp.ext.webserver.WebServerExtension;
import org.springframework.boot.SpringApplication;
import org.springframework.web.servlet.config.annotation.ResourceChainRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.resource.GzipResourceResolver;
import org.springframework.web.servlet.resource.PathResourceResolver;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Nullable;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseObject;
import php.runtime.memory.ArrayMemory;
import php.runtime.reflection.ClassEntity;

import java.util.ArrayList;
import java.util.List;

@Name("WebServer")
@Reflection.Namespace(WebServerExtension.NS)
public class PWebServer extends BaseObject {
    protected SpringApplication application;
    protected Invoker onRequest;
    protected List<ArrayMemory> staticHandlers = new ArrayList<ArrayMemory>();
    protected boolean hotReload;
    protected boolean isolated;

    public PWebServer(Environment env) {
        super(env);
    }

    public PWebServer(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public SpringApplication getApplication() {
        return application;
    }

    public Invoker getOnRequest() {
        return onRequest;
    }

    @Signature
    public void __construct() {
        __construct(null);
    }

    @Signature
    public void __construct(@Nullable Invoker invoker) {
        application = new SpringApplication(WebServerConfig.class);
        onRequest = invoker;

        WebServerController.registerServer(this);
        WebServerController.application = application;
    }

    public boolean isHotReload() {
        return hotReload;
    }

    public boolean isIsolated() {
        return isolated;
    }

    @Signature
    public PWebServer setRoute(Invoker invoker) {
        onRequest = invoker;
        return this;
    }

    @Signature
    public PWebServer setHotReload(boolean value) {
        this.hotReload = value;
        return this;
    }

    @Signature
    public PWebServer setIsolated(boolean value) {
        this.isolated = value;
        return this;
    }

    @Signature
    public PWebServer setPort(int value) {
        System.setProperty("server.port", String.valueOf(value));
        return this;
    }

    @Signature
    public PWebServer addDynamicHandler(Environment env, ArrayMemory handler) {
        if (handler.getByScalar("path") == null) {
            env.exception("Dynamic handler should contain a value the 'path' key");
        }

        return this;
    }

    @Signature
    public PWebServer addStaticHandler(Environment env, ArrayMemory handler) {
        if (handler.getByScalar("path") == null) {
            env.exception("Static handler should contain a value the 'path' key");
        }

        if (handler.getByScalar("location") == null) {
            env.exception("Static handler should contain a value in the 'location' key");
        }

        staticHandlers.add(handler.toImmutable().toValue(ArrayMemory.class));
        return this;
    }

    protected final static Object lock = new Object();

    protected WebServerConfig.Handlers _makeHandlers() {
        return new WebServerConfig.Handlers() {
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                for (ArrayMemory handler : staticHandlers) {
                    Memory path = handler.valueOfIndex("path");

                    ResourceHandlerRegistration registration;

                    if (path.isArray()) {
                        registration = registry.addResourceHandler(path.toValue(ArrayMemory.class).toStringArray());
                    } else {
                        registration = registry.addResourceHandler(path.toString());
                    }

                    Memory cachePeriod = handler.valueOfIndex("cachePeriod");
                    registration.setCachePeriod(cachePeriod.isNull() ? null : cachePeriod.toInteger());

                    Memory location = handler.valueOfIndex("location");

                    if (location.isArray()) {
                        registration.addResourceLocations(location.toValue(ArrayMemory.class).toStringArray());
                    } else {
                        registration.addResourceLocations(location.toString());
                    }

                    ResourceChainRegistration chain = registration.resourceChain(handler.valueOfIndex("cache").toBoolean());

                    if (handler.valueOfIndex("gzip").toBoolean()) {
                        chain.addResolver(new GzipResourceResolver());
                    }

                    chain.addResolver(new PathResourceResolver());
                }
            }
        };
    }

    @Signature
    public void run() {
        synchronized (lock) {
            WebServerConfig.Handlers old = WebServerConfig.HANDLERS;
            WebServerConfig.HANDLERS = _makeHandlers();

            application.run();

            WebServerConfig.HANDLERS = old;
        }
    }
}
