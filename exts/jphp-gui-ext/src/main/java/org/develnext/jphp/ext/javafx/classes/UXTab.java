package org.develnext.jphp.ext.javafx.classes;

import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import org.develnext.jphp.ext.javafx.support.EventProvider;
import org.develnext.jphp.ext.javafx.support.JavaFxUtils;
import org.develnext.jphp.ext.javafx.support.UserData;
import org.develnext.jphp.ext.javafx.support.control.tabpane.DndTabPane;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Environment;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

@Reflection.Name(JavaFXExtension.NS + "UXTab")
public class UXTab extends BaseWrapper<Tab> {
    interface WrappedInterface {
        @Property boolean closable();
        @Property boolean disable();
        @Property boolean disabled();

        @Property @Nullable Node content();
        @Property String id();
        @Property boolean selected();
        @Property String style();

        @Property @Nullable Tooltip tooltip();
        @Property @Nullable ContextMenu contextMenu();
    }

    public UXTab(Environment env, Tab wrappedObject) {
        super(env, wrappedObject);
    }

    public UXTab(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new Tab();
    }

    @Signature
    public void __construct(String title) {
        __wrappedObject = new Tab(title);
    }

    @Signature
    public void __construct(String title, Node content) {
        __wrappedObject = new Tab(title, content);
    }

    @Getter
    public String getText() {
        return getWrappedObject().getText();
    }

    @Setter
    public void setText(String value) {
        getWrappedObject().setText(value);
    }

    @Getter
    public Node getGraphic() {
        return getWrappedObject().getGraphic();
    }

    @Setter
    public void setGraphic(@Nullable Node node) {
        getWrappedObject().setGraphic(node);
    }

    @Getter
    public Memory getUserData(Environment env) {
        return JavaFxUtils.userData(env, getWrappedObject().getUserData());
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
    public Memory data(String name) {
        return JavaFxUtils.data(getWrappedObject(), name);
    }

    @Signature
    public Memory data(Environment env, String name, Memory value) {
        return JavaFxUtils.data(env, getWrappedObject(), name, value);
    }

    @Setter
    public void setDraggable(boolean value) {
        TabPane tabPane = getWrappedObject().getTabPane();

        if (tabPane instanceof DndTabPane) {
            ((DndTabPane) tabPane).setDraggableTab(getWrappedObject(), value);
        }
    }

    @Getter
    public boolean getDraggable() {
        TabPane tabPane = getWrappedObject().getTabPane();

        if (tabPane instanceof DndTabPane) {
            return ((DndTabPane) tabPane).isDraggableTab(getWrappedObject());
        }

        return false;
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
}
