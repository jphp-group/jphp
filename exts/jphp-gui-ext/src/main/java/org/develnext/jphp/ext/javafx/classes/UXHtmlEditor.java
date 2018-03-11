package org.develnext.jphp.ext.javafx.classes;

import javafx.scene.web.HTMLEditor;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Name(JavaFXExtension.NS + "UXHtmlEditor")
public class UXHtmlEditor extends UXControl {
    interface WrappedInterface {
        @Property String htmlText();
    }

    public UXHtmlEditor(Environment env, HTMLEditor wrappedObject) {
        super(env, wrappedObject);
    }

    public UXHtmlEditor(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public HTMLEditor getWrappedObject() {
        return (HTMLEditor) super.getWrappedObject();
    }

    @Signature
    public void __construct() {
        __wrappedObject = new HTMLEditor();
    }
}
