package org.develnext.jphp.ext.javafx.classes.paint;

import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

@Reflection.Final
@Reflection.Name(JavaFXExtension.NS + "paint\\UXStop")
public class UXStop extends BaseWrapper<Stop>{
    public UXStop(Environment env, Stop wrappedObject) {
        super(env, wrappedObject);
    }
    public UXStop(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    interface WrappedInterface{
        @Reflection.Getter Color getColor();
        @Reflection.Getter double getOffset();
    }

    @Reflection.Signature
    public void __construct(double offset, Color color){
        __wrappedObject = new Stop(offset, color);
    }
}
