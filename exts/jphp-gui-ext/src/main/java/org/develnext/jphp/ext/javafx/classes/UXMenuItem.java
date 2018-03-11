package org.develnext.jphp.ext.javafx.classes;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCombination;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import org.develnext.jphp.ext.javafx.support.EventProvider;
import org.develnext.jphp.ext.javafx.support.UserData;
import php.runtime.Memory;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Environment;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

@Name(JavaFXExtension.NS + "UXMenuItem")
public class UXMenuItem extends BaseWrapper<MenuItem> {
    interface WrappedInterface {
        @Property String id();
        @Property String style();

        @Property("classes")
        ObservableList<String> styleClass();

        @Property @Nullable Node graphic();
        @Property(hiddenInDebugInfo = true) ContextMenu parentPopup();

        @Property boolean disable();
        @Property boolean visible();

        @Property boolean mnemonicParsing();

        @Getter KeyCombination getAccelerator();
        @Setter void setAccelerator(@Nullable KeyCombination keyCombination);
    }

    public UXMenuItem(Environment env, SeparatorMenuItem wrappedObject) {
        super(env, wrappedObject);
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
        __wrappedObject = new MenuItem(text, graphic == null ? null : graphic.getWrappedObject());
    }

    @Getter
    public boolean getEnabled() {
        return !getWrappedObject().isDisable();
    }

    @Setter
    public void setEnabled(boolean value) {
        getWrappedObject().setDisable(!value);
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

    @Getter
    public String getText() {
        return getWrappedObject().getText();
    }

    @Setter
    public void setText(String value) {
        getWrappedObject().setText(value);
    }

    @Signature
    public boolean isSeparator() {
        return getWrappedObject() instanceof SeparatorMenuItem;
    }

    @Getter
    public Memory getUserData(Environment env) {
        Object userData = getWrappedObject().getUserData();

        if (userData == null) {
            return null;
        }

        if (userData instanceof UserData) {
            return ((UserData) userData).getValue();
        }

        return Memory.wrap(env, userData);
    }

    @Setter
    public void setUserData(Environment env, @Nullable Object value) {
        Object userData = getWrappedObject().getUserData();

        if (userData instanceof UserData) {
            ((UserData) userData).setValue(Memory.wrap(env, value));
        } else {
            getWrappedObject().setUserData(value);
        }
    }

    @Signature
    public static UXMenuItem createSeparator(Environment env) {
        return new UXMenuItem(env, new SeparatorMenuItem());
    }
}
