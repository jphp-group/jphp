package org.develnext.jphp.ext.javafx.classes;

import javafx.scene.paint.Color;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import org.develnext.jphp.ext.javafx.support.control.FlatButton;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Nullable;
import php.runtime.annotation.Reflection.Property;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Reflection.Name(JavaFXExtension.NS + "UXFlatButton")
public class UXFlatButton extends UXButtonBase<FlatButton> {
    interface WrappedInterface {
        @Property Color color();
        @Property @Nullable Color hoverColor();
        @Property @Nullable Color clickColor();
        @Property double borderRadius();
    }

    public UXFlatButton(Environment env, FlatButton wrappedObject) {
        super(env, wrappedObject);
    }

    public UXFlatButton(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Reflection.Signature
    public void __construct() {
        __wrappedObject = new FlatButton();
    }

    @Reflection.Signature
    public void __construct(String text) {
        __wrappedObject = new FlatButton(text);
    }
}
