package org.develnext.jphp.ext.javafx.classes;

import javafx.scene.control.ComboBoxBase;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Abstract
@Name(JavaFXExtension.NS + "UXComboBoxBase")
public class UXComboBoxBase<T extends ComboBoxBase> extends UXControl<ComboBoxBase> {
    interface WrappedInterface {
        @Property boolean armed();
        @Property boolean editable();
        @Property boolean showing();
        @Property String promptText();

        void arm();
        void disarm();
    }

    public UXComboBoxBase(Environment env, T wrappedObject) {
        super(env, wrappedObject);
    }

    public UXComboBoxBase(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void showPopup() {
        getWrappedObject().show();
    }

    @Signature
    public void hidePopup() {
        getWrappedObject().hide();
    }

    @Getter
    public boolean getPopupVisible() {
        return getWrappedObject().isShowing();
    }

    @Setter
    public void setPopupVisible(boolean value) {
        if (value) {
            showPopup();
        } else {
            hidePopup();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getWrappedObject() {
        return (T) super.getWrappedObject();
    }

    @Getter
    protected Memory getValue(Environment env) {
        return Memory.wrap(env, getWrappedObject().getValue());
    }

    @Setter
    protected void setValue(Environment env, Memory value) {
        getWrappedObject().setValue(Memory.unwrap(env, value));
    }
}
