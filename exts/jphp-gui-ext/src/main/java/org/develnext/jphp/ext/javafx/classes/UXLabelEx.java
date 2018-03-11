package org.develnext.jphp.ext.javafx.classes;

import javafx.scene.Node;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import org.develnext.jphp.ext.javafx.support.control.LabelEx;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Name(JavaFXExtension.NS + "UXLabelEx")
public class UXLabelEx extends UXLabel<LabelEx> {
    interface WrappedInterface {
        @Property boolean autoSize();
        @Property LabelEx.AutoSizeType autoSizeType();
    }

    public UXLabelEx(Environment env, LabelEx wrappedObject) {
        super(env, wrappedObject);
    }

    public UXLabelEx(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new LabelEx();
    }

    @Signature
    public void __construct(String text) {
        __wrappedObject = new LabelEx(text);
    }

    @Signature
    public void __construct(String text, Node graphic) {
        __wrappedObject = new LabelEx(text, graphic);
    }
}
