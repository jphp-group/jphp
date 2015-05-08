package org.develnext.jphp.ext.javafx.classes;

import javafx.scene.control.PopupControl;
import javafx.stage.PopupWindow;
import javafx.stage.Window;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Nullable;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Name(JavaFXExtension.NAMESPACE + "UXPopupWindow")
public class UXPopupWindow<T extends PopupWindow> extends UXWindow<PopupWindow> {
    interface WrappedInterface {
        @Property boolean autoFix();
        @Property boolean autoHide();
        @Property boolean hideOnEscape();

        void show(Window owner);
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

    @Signature
    public void __construct() {
        __wrappedObject = new PopupControl();
    }
}
