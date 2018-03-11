package org.develnext.jphp.ext.javafx.classes;

import org.develnext.jphp.ext.javafx.JavaFXExtension;
import org.develnext.jphp.ext.javafx.support.control.MaskTextField;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Name(JavaFXExtension.NS + "UXMaskTextField")
public class UXMaskTextField extends UXTextField<MaskTextField> {
    interface WrappedInterface {
        @Property String plainText();
        @Property String placeholder();
        @Property String mask();
        @Property String whatMask();
    }

    public UXMaskTextField(Environment env, MaskTextField wrappedObject) {
        super(env, wrappedObject);
    }

    public UXMaskTextField(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    @Signature
    public void __construct(String text) {
        __wrappedObject = new MaskTextField(text);
        fixContextMenu();
    }
}
