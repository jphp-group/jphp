package org.develnext.jphp.ext.javafx.classes;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.Event;
import javafx.print.PrinterJob;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import netscape.javascript.JSObject;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import org.develnext.jphp.ext.javafx.support.EventProvider;
import org.develnext.jphp.ext.javafx.support.JavaFxUtils;
import org.w3c.dom.Document;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Environment;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseWrapper;
import php.runtime.memory.ArrayMemory;
import php.runtime.reflection.ClassEntity;

import java.io.File;

@Abstract
@Name(JavaFXExtension.NS + "UXWebEngine")
public class UXWebEngine extends BaseWrapper<WebEngine> {
    interface WrappedInterface {
        @Property Document document();
        @Property WebHistory history();
        @Property boolean javaScriptEnabled();
        @Property String location();
        @Property String title();

        @Property String userAgent();
        @Property String userStyleSheetLocation();
        @Property File userDataDirectory();

        void load(String url);
        void loadContent(String content);
        void loadContent(String content, String contentType);

        void reload();
    }

    public UXWebEngine(Environment env, WebEngine wrappedObject) {
        super(env, wrappedObject);
    }

    public UXWebEngine(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Setter
    public void setUrl(String url) {
        getWrappedObject().load(url);
    }

    @Getter
    public String getUrl() {
        return getWrappedObject().getLocation();
    }

    @Signature
    public Memory executeScript(Environment env, String script) {
        return Memory.wrap(env, getWrappedObject().executeScript(script));
    }

    @Signature
    public void refresh() {
        getWrappedObject().reload();
    }

    @Signature
    public boolean cancel() {
        return getWrappedObject().getLoadWorker().cancel();
    }

    @Signature
    public Memory callFunction(Environment env, String name, ArrayMemory args) {
        JSObject window = (JSObject) getWrappedObject().executeScript("window");

        if (window == null) {
            throw new IllegalStateException("Unable to find window object");
        }

        return Memory.wrap(env, window.call(name, args.toStringArray()));
    }

    public static class Bridge {
        protected final Invoker handler;

        public Bridge(Invoker handler) {
            this.handler = handler;
        }

        public String run(String arg1) {
            return handler.callAny(arg1).toString();
        }
    }

    @Signature
    public void addSimpleBridge(Environment env, String name, final Invoker handler) {
        JSObject window = (JSObject) getWrappedObject().executeScript("window");
        window.setMember(name, new Bridge(handler));
    }

    @Signature
    public ObservableValue observer(String property) {
        return JavaFxUtils.findObservable(this, property);
    }

    @Getter
    public Worker.State getState() {
        return getWrappedObject().getLoadWorker().getState();
    }

    @Signature
    public void watchState(final Environment env, final Invoker invoker) {
        getWrappedObject().getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue<? extends Worker.State> observable, final Worker.State oldValue, final Worker.State newValue) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            invoker.callAny(UXWebEngine.this, oldValue, newValue);
                        } catch (Throwable e) {
                            env.wrapThrow(e);
                        }
                    }
                });
            }
        });
    }

    @Signature
    @SuppressWarnings("unchecked")
    public void on(String event, Invoker invoker, String group) {
        Object target = getWrappedObject();
        EventProvider eventProvider = EventProvider.get(target, event);

        if (eventProvider != null) {
            eventProvider.on(target, event, group, invoker);
        } else {
            throw new IllegalArgumentException("Unable to find the '"+event+"' event type");
        }
    }

    @Signature
    public void on(String event, Invoker invoker) {
        on(event, invoker, "general");
    }

    @Signature
    @SuppressWarnings("unchecked")
    public void off(String event, @Reflection.Nullable String group) {
        Object target = getWrappedObject();
        EventProvider eventProvider = EventProvider.get(target, event);

        if (eventProvider != null) {
            eventProvider.off(target, event, group);
        } else {
            throw new IllegalArgumentException("Unable to find the '"+event+"' event type");
        }
    }

    @Signature
    public void off(String event) {
        off(event, null);
    }

    @Signature
    public void trigger(String event, @Reflection.Nullable Event e) {
        Object target = getWrappedObject();
        EventProvider eventProvider = EventProvider.get(target, event);

        if (eventProvider != null) {
            eventProvider.trigger(target, event, e);
        } else {
            throw new IllegalArgumentException("Unable to find the '"+event+"' event type");
        }
    }

    @Signature
    public void print(PrinterJob printerJob) {
        getWrappedObject().print(printerJob);
    }
}
