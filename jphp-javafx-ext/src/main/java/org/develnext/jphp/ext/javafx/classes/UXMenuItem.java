package org.develnext.jphp.ext.javafx.classes;

import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import org.develnext.jphp.ext.javafx.support.EventProvider;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Environment;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

@Name(JavaFXExtension.NAMESPACE + "UXMenuItem")
public class UXMenuItem extends BaseWrapper<MenuItem> {
    interface WrappedInterface {
        @Property String id();
        @Property String text();
        @Property String style();
        @Property Node graphic();
        @Property ContextMenu parentPopup();

        @Property boolean disable();
        @Property boolean visible();

        @Property boolean mnemonicParsing();

        @Getter KeyCombination getAccelerator();
        @Setter void setAccelerator(@Nullable KeyCombination keyCombination);
    }

    public UXMenuItem(Environment env, MenuItem wrappedObject) {
        super(env, wrappedObject);
    }

    public UXMenuItem(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new MenuItem();
    }

    @Signature
    public void __construct(String text) {
        __wrappedObject = new MenuItem(text);
    }

    @Signature
    public void __construct(String text, @Nullable UXNode graphic) {
        __wrappedObject = new MenuItem(text, graphic.getWrappedObject());
    }

    @Signature
    @SuppressWarnings("unchecked")
    public void on(String event, Invoker invoker, String group) {
        EventProvider eventProvider = EventProvider.get(getWrappedObject(), event);

        if (eventProvider != null) {
            eventProvider.on(getWrappedObject(), event, group, invoker);
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
    public void off(String event, @Nullable String group) {
        EventProvider eventProvider = EventProvider.get(getWrappedObject(), event);

        if (eventProvider != null) {
            eventProvider.off(getWrappedObject(), event, group);
        } else {
            throw new IllegalArgumentException("Unable to find the '"+event+"' event type");
        }
    }

    @Signature
    public void off(String event) {
        off(event, null);
    }

    @Signature
    public void trigger(String event, @Nullable Event e) {
        EventProvider eventProvider = EventProvider.get(getWrappedObject(), event);

        if (eventProvider != null) {
            eventProvider.trigger(getWrappedObject(), event, e);
        } else {
            throw new IllegalArgumentException("Unable to find the '"+event+"' event type");
        }
    }
}
