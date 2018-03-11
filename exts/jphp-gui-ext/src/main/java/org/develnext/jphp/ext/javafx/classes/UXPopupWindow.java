package org.develnext.jphp.ext.javafx.classes;

import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.PopupControl;
import javafx.stage.PopupWindow;
import javafx.stage.Window;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Name(JavaFXExtension.NS + "UXPopupWindow")
public class UXPopupWindow<T extends PopupWindow> extends UXWindow<PopupWindow> {
    interface WrappedInterface {
        @Property boolean autoFix();
        @Property boolean autoHide();
        @Property boolean hideOnEscape();

        void show(@Nullable Window owner);
        void show(@Nullable Window owner, double screenX, double screenY);

        void hide();
    }

    public UXPopupWindow(Environment env, T wrappedObject) {
        super(env, wrappedObject);
    }

    public UXPopupWindow(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getWrappedObject() {
        return (T) super.getWrappedObject();
    }

    @Getter
    public ObservableList<String> getClasses() {
        T object = getWrappedObject();

        if (object instanceof PopupControl) {
            return ((PopupControl) object).getStyleClass();
        }

        return null;
    }

    @Getter
    public String getStyle() {
        T object = getWrappedObject();

        if (object instanceof PopupControl) {
            return ((PopupControl) object).getStyle();
        }

        return null;
    }

    @Setter
    public void setStyle(String value) {
        T object = getWrappedObject();

        if (object instanceof PopupControl) {
            ((PopupControl) object).setStyle(value);
        }
    }

    @Signature
    public void __construct() {
        __wrappedObject = new PopupControl();
    }

    @Signature
    public void showByNode(Node node) {
        showByNode(node, 0, 0);
    }

    @Signature
    public void showByNode(Node node, int offsetX, int offsetY) {
        Bounds screen = node.localToScreen(node.getLayoutBounds());

        getWrappedObject().show(node.getScene().getWindow(), screen.getMinX() + offsetX, screen.getMinY() + offsetY);
    }
}
