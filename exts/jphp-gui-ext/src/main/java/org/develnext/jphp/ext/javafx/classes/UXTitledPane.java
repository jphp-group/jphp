package org.develnext.jphp.ext.javafx.classes;

import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Nullable;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Reflection.Name(JavaFXExtension.NS + "UXTitledPane")
public class UXTitledPane extends UXLabeled<TitledPane> {
    interface WrappedInterface {
        @Property Node content();

        @Property boolean animated();
        @Property boolean expanded();
        @Property boolean collapsible();
    }

    public UXTitledPane(Environment env, TitledPane wrappedObject) {
        super(env, wrappedObject);
    }

    public UXTitledPane(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new TitledPane();
    }

    @Signature
    public void __construct(String title) {
        __wrappedObject = new TitledPane(title, null);
    }

    @Signature
    public void __construct(String title, @Nullable Node content) {
        __wrappedObject = new TitledPane(title, content);
    }
}
