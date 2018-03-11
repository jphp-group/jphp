package org.develnext.jphp.ext.javafx.classes;

import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Window;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.stream.ResourceStream;
import php.runtime.ext.core.classes.stream.Stream;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

import java.util.List;

@Name(JavaFXExtension.NS + "UXScene")
public class UXScene extends BaseWrapper<Scene> {
    interface WrappedInterface {
        @Property Parent root();
        @Property Window window();
    }

    public UXScene(Environment env, Scene wrappedObject) {
        super(env, wrappedObject);
    }

    public UXScene(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct(Parent parent) {
        __wrappedObject = new Scene(parent);
    }

    @Signature
    public void __construct(Parent parent, double[] size) {
        if (size.length >= 2) {
            __wrappedObject = new Scene(parent, size[0], size[1]);
        } else {
            __wrappedObject = new Scene(parent);
        }
    }

    @Reflection.Getter
    protected Cursor getCursor() {
        return getWrappedObject().getCursor();
    }

    @Reflection.Setter
    protected void setCursor(Cursor cursor) {
        getWrappedObject().setCursor(cursor);
    }

    @Signature
    public void clearStylesheets() {
        getWrappedObject().getStylesheets().clear();
    }

    @Signature
    public List<String> getStylesheets() {
        return getWrappedObject().getStylesheets();
    }

    @Signature
    public void addStylesheet(Memory value) {
        if (value.instanceOf(Stream.class)) {
            if (value.instanceOf(ResourceStream.class)) {
                getWrappedObject().getStylesheets().addAll(value.toObject(ResourceStream.class).getUrl().toExternalForm());
            } else {
                getWrappedObject().getStylesheets().addAll(value.toObject(Stream.class).getPath());
            }
        } else {
            getWrappedObject().getStylesheets().addAll(value.toString());
        }
    }
}
