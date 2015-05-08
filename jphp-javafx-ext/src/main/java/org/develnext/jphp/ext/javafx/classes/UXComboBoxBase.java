package org.develnext.jphp.ext.javafx.classes;

import javafx.scene.control.ComboBoxBase;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Abstract
@Name(JavaFXExtension.NAMESPACE + "UXComboBoxBase")
public class UXComboBoxBase extends UXControl {
    interface WrappedInterface {
        @Property boolean armed();
        @Property boolean editable();
        @Property boolean showing();
        @Property String promptText();

        void arm();
        void disarm();

        void hide();
        void show();
    }

    public UXComboBoxBase(Environment env, ComboBoxBase wrappedObject) {
        super(env, wrappedObject);
    }

    public UXComboBoxBase(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ComboBoxBase getWrappedObject() {
        return (ComboBoxBase) super.getWrappedObject();
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
