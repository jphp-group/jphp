package org.develnext.jphp.ext.javafx.classes;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Abstract;
import php.runtime.annotation.Reflection.Property;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

import java.util.List;

@Abstract
@Reflection.Name(JavaFXExtension.NS + "UXScreen")
public class UXScreen extends BaseWrapper<Screen> {
    interface WrappedInterface {
        @Property double dpi();
        @Property Rectangle2D bounds();
        @Property Rectangle2D visualBounds();
    }

    public UXScreen(Environment env, Screen wrappedObject) {
        super(env, wrappedObject);
    }

    public UXScreen(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Reflection.Signature
    public static Screen getPrimary() {
        return Screen.getPrimary();
    }

    @Reflection.Signature
    public static List<Screen> getScreens() {
        return Screen.getScreens();
    }
}
