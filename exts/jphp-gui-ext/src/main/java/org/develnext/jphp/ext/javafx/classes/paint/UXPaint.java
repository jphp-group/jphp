package org.develnext.jphp.ext.javafx.classes.paint;

import javafx.scene.paint.Paint;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

@Reflection.Name(JavaFXExtension.NS + "paint\\UXPaint")
@Reflection.Abstract
public class UXPaint<T extends Paint> extends BaseWrapper<T>{
    public UXPaint(Environment env, T wrappedObject) {
        super(env, wrappedObject);
    }
    public UXPaint(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    interface WrappedInterface{
        @Reflection.Getter("opaque") boolean isOpaque();
    }

    @Reflection.Signature
    public static Paint of(String value){
        try{
            return Paint.valueOf(value);
        }
        catch(IllegalArgumentException e){
            return null;
        }
    }
}