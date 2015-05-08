package org.develnext.jphp.ext.javafx.classes;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.scene.DepthTest;
import javafx.scene.Node;
import javafx.scene.effect.BlendMode;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import org.develnext.jphp.ext.javafx.support.EventProvider;
import php.runtime.Memory;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseWrapper;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.support.MemoryOperation;
import php.runtime.reflection.ClassEntity;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

@Abstract
@Name(JavaFXExtension.NAMESPACE + "UXNode")
public class UXNode extends BaseWrapper<Node> {
    interface WrappedInterface {
        @Property double baselineOffset();
        @Property BlendMode blendMode();
        @Property Node clip();
        @Property Orientation contentBias();
        @Property DepthTest depthTest();
        @Property String id();
        @Property("x") double layoutX();
        @Property("y") double layoutY();
        @Property double opacity();

        @Property double rotate();

        @Property double scaleX();
        @Property double scaleY();
        @Property double scaleZ();

        @Property String style();

        @Property double translateX();
        @Property double translateY();
        @Property double translateZ();

        @Property boolean cache();
        @Property boolean disable();
        @Property boolean disabled();
        @Property boolean focused();
        @Property boolean focusTraversable();
        @Property boolean hover();
        @Property boolean managed();
        @Property boolean mouseTransparent();
        @Property boolean pickOnBounds();
        @Property boolean pressed();
        @Property boolean resizable();
        @Property boolean visible();

        @Property("classes") ObservableList<String> styleClass();

        void autosize();
        boolean contains(double localX, double localY);

        void relocate(double x, double y);
        void resize(double width, double height);
        void startFullDrag();

        double maxHeight(double width);
        double maxWidth(double height);
        double minHeight(double width);
        double minWidth(double height);

        double prefHeight(double width);
        double prefWidth(double height);

        void toBack();
        void toFront();
    }

    public UXNode(Environment env, Node wrappedObject) {
        super(env, wrappedObject);
    }

    public UXNode(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public UXNode getRealObject() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<? extends BaseWrapper> wrapperClass = MemoryOperation.getWrapper(getWrappedObject().getClass());
        if (wrapperClass != null) {
            return (UXNode) wrapperClass
                    .getConstructor(Environment.class, getWrappedObject().getClass())
                    .newInstance(getEnvironment(), getWrappedObject());
        } else {
            return this;
        }
    }

    @Getter
    protected double[] getPosition() {
        return new double[] { getWrappedObject().getLayoutX(), getWrappedObject().getLayoutY() };
    }

    @Setter
    protected void setPosition(double[] value) {
        if (value.length >= 2) {
            getWrappedObject().setLayoutX(value[0]);
            getWrappedObject().setLayoutY(value[1]);
        }
    }

    @Getter
    protected double[] getSize() {
        Bounds bounds = getWrappedObject().getLayoutBounds();
        return new double[] { bounds.getWidth(), bounds.getHeight() };
    }

    @Setter
    protected void setSize(double[] size) {
        if (size.length >= 2) {
            getWrappedObject().prefWidth(size[0]);
            getWrappedObject().prefHeight(size[1]);
        }
    }

    @Getter
    protected double getWidth() {
        return getWrappedObject().getLayoutBounds().getWidth();
    }

    @Setter
    protected void setWidth(double v) {
        getWrappedObject().prefWidth(v);
    }

    @Getter
    protected double getHeight() {
        return getWrappedObject().getLayoutBounds().getHeight();
    }

    @Setter
    protected void setHeight(double v) {
        getWrappedObject().prefHeight(v);
    }

    @Signature
    public void show() {
        getWrappedObject().setVisible(true);
    }

    @Signature
    public void hide() {
        getWrappedObject().setVisible(false);
    }

    @Signature
    public void toggle() {
        getWrappedObject().setVisible(!getWrappedObject().isVisible());
    }

    @Signature
    @SuppressWarnings("unchecked")
    public Memory lookup(Environment env, TraceInfo trace, String selector) {
        Node result = getWrappedObject().lookup(selector);

        if (result == null) {
            return null;
        }

        return MemoryOperation.get(result.getClass(), null).unconvert(env, trace, result);
    }

    @Signature
    @SuppressWarnings("unchecked")
    public Memory lookupAll(Environment env, TraceInfo trace, String selector) {
        Set<Node> result = getWrappedObject().lookupAll(selector);

        ArrayMemory r = new ArrayMemory();

        for (Node node : result) {
            Memory el = MemoryOperation.get(node.getClass(), null).unconvert(env, trace, node);
            r.add(el);
        }

        return r.toConstant();
    }

    @Getter
    protected UXParent getParent(Environment env) {
        if (getWrappedObject().getParent() == null) {
            return null;
        }

        return new UXParent(env, getWrappedObject().getParent());
    }

    @Getter
    protected UXScene getScene(Environment env) {
        if (getWrappedObject().getScene() == null) {
            return null;
        }

        return new UXScene(env, getWrappedObject().getScene());
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
