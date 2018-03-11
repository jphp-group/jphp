package org.develnext.jphp.ext.javafx.classes;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Nullable;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

@Reflection.Name(JavaFXExtension.NS + "UXTreeItem")
public class UXTreeItem extends BaseWrapper<TreeItem> {
    interface WrappedInterface {
        @Property boolean expanded();
        @Property Object value();
        @Property @Nullable Node graphic();
        @Property TreeItem parent();

        @Property ObservableList<TreeItem> children();
    }

    public UXTreeItem(Environment env, TreeItem wrappedObject) {
        super(env, wrappedObject);
    }

    public UXTreeItem(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __construct(null);
    }

    @Signature
    @SuppressWarnings("unchecked")
    public void __construct(Memory value) {
        __wrappedObject = new TreeItem(value);
    }

    @Signature
    public void update() {
        Object value = getWrappedObject().getValue();

        getWrappedObject().setValue(null);
        getWrappedObject().setValue(value);
    }
}
