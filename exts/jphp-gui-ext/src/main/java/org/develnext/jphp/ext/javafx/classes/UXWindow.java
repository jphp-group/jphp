package org.develnext.jphp.ext.javafx.classes;

import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.SplitPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import org.develnext.jphp.ext.javafx.support.EventProvider;
import org.develnext.jphp.ext.javafx.support.JavaFxUtils;
import org.develnext.jphp.ext.javafx.support.UserData;
import php.runtime.Memory;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Environment;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Abstract
@Name(JavaFXExtension.NS + "UXWindow")
public class UXWindow<T extends Window> extends BaseWrapper<Window> {
    interface WrappedInterface {
        @Property double x();
        @Property double y();
        @Property double height();
        @Property double width();
        @Property double opacity();

        @Property boolean focused();
        @Property("visible") boolean showing();

        void hide();
        void sizeToScene();

        void centerOnScreen();
        void requestFocus();
    }

    public UXWindow(Environment env, T wrappedObject) {
        super(env, wrappedObject);
    }

    public UXWindow(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getWrappedObject() {
        return (T) super.getWrappedObject();
    }

    @Signature
    public void __construct() {
        __wrappedObject = new Stage();
    }

    @Signature
    public void __construct(Stage stage) {
        __wrappedObject = stage;
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
    public Memory data(String name) {
        return JavaFxUtils.data(getWrappedObject(), name);
    }

    @Signature
    public Memory data(Environment env, String name, Memory value) {
        return JavaFxUtils.data(env, getWrappedObject(), name, value);
    }

    @Getter
    protected Cursor getCursor() {
        return getWrappedObject().getScene().getCursor();
    }

    @Setter
    protected void setCursor(Cursor cursor) {
        getWrappedObject().getScene().setCursor(cursor);
    }

    @Getter
    protected double[] getSize() {
        return new double[] { getWrappedObject().getWidth(), getWrappedObject().getHeight() };
    }

    @Setter
    protected void setSize(double[] size) {
        if (size.length >= 2) {
            getWrappedObject().setWidth(size[0]);
            getWrappedObject().setHeight(size[1]);
        }
    }

    @Getter
    public Parent getLayout(Environment env) {
        Memory data = data("~~virtual-layout");

        if (data.isNotNull()) {
            return (Parent) Memory.unwrap(env, data);
        }


        return getWrappedObject().getScene() == null ? null : getWrappedObject().getScene().getRoot();
    }

    @Setter
    public void setLayout(Environment env, Parent pane) {
        if (getWrappedObject().getScene() == null) {
            throw new IllegalStateException("Unable to set layout");
        }

        getWrappedObject().getScene().setRoot(pane);
        getWrappedObject().sizeToScene();
        data(env, "~~virtual-layout", Memory.NULL);
    }

    @Signature
    public void makeVirtualLayout(Environment env) {
        Parent layout = getLayout(env);
        data(env, "~~virtual-layout", Memory.wrap(env, layout));
        getWrappedObject().getScene().setRoot(new AnchorPane());
    }

    @Getter
    public ObservableList<Node> getChildren(Environment env) {
        Parent root = getLayout(env);
        if (root instanceof Pane) {
            return ((Pane) root).getChildren();
        } else if (root instanceof SplitPane) {
            return ((SplitPane) root).getItems();
        } else {
            return FXCollections.emptyObservableList();
        }
    }

    @Signature
    public void add(Environment env, Node node) {
        ObservableList<Node> children = getChildren(env);

        if (children != null) {
            children.add(node);
        } else {
            throw new IllegalStateException("Unable to add node");
        }
    }

    @Signature
    public boolean remove(Environment env, Node node) {
        ObservableList<Node> children = getChildren(env);

        if (children != null) {
            return children.remove(node);
        } else {
            throw new IllegalStateException("Unable to remove node");
        }
    }

    @Signature
    public boolean hasStylesheet(String path) {
        return getWrappedObject().getScene().getStylesheets().contains(path);
    }

    @Signature
    public void addStylesheet(String path) {
        getWrappedObject().getScene().getStylesheets().add(path);
    }

    @Signature
    public void removeStylesheet(String path) {
        getWrappedObject().getScene().getStylesheets().remove(path);
    }

    @Signature
    public void clearStylesheets() {
        getWrappedObject().getScene().getStylesheets().clear();
    }

    @Signature
    public void addEventFilter(final Environment env, String event, final Invoker invoker) {
        EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    invoker.callAny(event);
                } catch (Throwable throwable) {
                    env.wrapThrow(throwable);
                }
            }
        };

        EventHandler<KeyEvent> keyHandler = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                try {
                    invoker.callAny(event);
                } catch (Throwable throwable) {
                    env.wrapThrow(throwable);
                }
            }
        };

        switch (event) {
            case "mouseMove":
                getWrappedObject().getScene().addEventFilter(MouseEvent.MOUSE_MOVED, mouseHandler);
                break;
            case "mouseDrag":
                getWrappedObject().getScene().addEventFilter(MouseEvent.MOUSE_DRAGGED, mouseHandler);
                break;
            case "mouseDown":
                getWrappedObject().getScene().addEventFilter(MouseEvent.MOUSE_PRESSED, mouseHandler);
                break;
            case "mouseUp":
                getWrappedObject().getScene().addEventFilter(MouseEvent.MOUSE_RELEASED, mouseHandler);
                break;
            case "click":
                getWrappedObject().getScene().addEventFilter(MouseEvent.MOUSE_CLICKED, mouseHandler);
                break;
            case "keyDown":
                getWrappedObject().getScene().addEventFilter(KeyEvent.KEY_PRESSED, keyHandler);
                break;
            case "keyUp":
                getWrappedObject().getScene().addEventFilter(KeyEvent.KEY_RELEASED, keyHandler);
                break;
            case "keyPress":
                getWrappedObject().getScene().addEventFilter(KeyEvent.KEY_TYPED, keyHandler);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    @Signature
    @SuppressWarnings("unchecked")
    public void on(String event, Invoker invoker, String group, Environment env) {
        Object target = getWrappedObject();
        EventProvider eventProvider = EventProvider.get(target, event);

        if (eventProvider == null) {
            eventProvider = EventProvider.get(target = getLayout(env), event);
        }

        if (eventProvider != null) {
            eventProvider.on(target, event, group, invoker);
        } else {
            throw new IllegalArgumentException("Unable to find the '"+event+"' event type");
        }
    }

    @Signature
    public void on(String event, Invoker invoker, Environment env) {
        on(event, invoker, "general", env);
    }

    @Signature
    @SuppressWarnings("unchecked")
    public void off(Environment env, String event, @Nullable String group) {
        Object target = getWrappedObject();
        EventProvider eventProvider = EventProvider.get(target, event);

        if (eventProvider == null) {
            eventProvider = EventProvider.get(target = getLayout(env), event);
        }

        if (eventProvider != null) {
            eventProvider.off(target, event, group);
        } else {
            throw new IllegalArgumentException("Unable to find the '"+event+"' event type");
        }
    }

    @Signature
    public void off(Environment env, String event) {
        off(env, event, null);
    }

    @Signature
    public void trigger(Environment env, String event, @Nullable Event e) {
        Object target = getWrappedObject();
        EventProvider eventProvider = EventProvider.get(target, event);

        if (eventProvider == null) {
            eventProvider = EventProvider.get(target = getLayout(env), event);
        }

        if (eventProvider != null) {
            eventProvider.trigger(target, event, e);
        } else {
            throw new IllegalArgumentException("Unable to find the '"+event+"' event type");
        }
    }

    @Signature
    public ObservableValue observer(String property) {
        return JavaFxUtils.findObservable(this.getWrappedObject(), property);
    }

    @Signature
    public void watch(final String property, final Invoker invoker) throws InvocationTargetException, IllegalAccessException {
        String name = property + "Property";

        Class<? extends Window> aClass = getWrappedObject().getClass();

        try {
            Method method = aClass.getMethod(name);

            ReadOnlyProperty bindProperty = (ReadOnlyProperty) method.invoke(getWrappedObject());

            bindProperty.addListener(new ChangeListener() {
                @Override
                public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                    invoker.callAny(UXWindow.this, property, oldValue, newValue);
                }
            });
        } catch (NoSuchMethodException | ClassCastException e) {
            throw new IllegalArgumentException("Unable to find the '" + property + "' property for watching");
        }
    }

    @Signature
    public Memory __get(Environment env, String name) throws NoSuchFieldException, IllegalAccessException {
        Memory prop = data("--property-" + name);

        if (prop.isNotNull()) {
            return prop;
        }

        Node node = UXNode.__globalLookup(getLayout(env), "#" + name);

        /*if (node instanceof MenuButton && node.getClass().getName().endsWith("MenuBarButton")) {
            Field field = node.getClass().getDeclaredField("menu");
            field.setAccessible(true);
            return Memory.wrap(env, field.get(node));
        }*/

        return Memory.wrap(env, node);
    }

    @Signature
    public boolean __isset(Environment env, String name) {
        return UXNode.__globalLookup(getLayout(env), "#" + name) != null;
    }
}
