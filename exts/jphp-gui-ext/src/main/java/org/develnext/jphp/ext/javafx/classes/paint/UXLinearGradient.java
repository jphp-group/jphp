package org.develnext.jphp.ext.javafx.classes.paint;

import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Stop;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

import java.util.List;

@Reflection.Name(JavaFXExtension.NS + "paint\\UXLinearGradient")
public class UXLinearGradient extends UXPaint<LinearGradient>{
    public UXLinearGradient(Environment env, LinearGradient wrappedObject) {
        super(env, wrappedObject);
    }
    public UXLinearGradient(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    interface WrappedInterface{
        @Reflection.Getter CycleMethod getCycleMethod();

        @Reflection.Getter double getEndX();
        @Reflection.Getter double getEndY();

        @Reflection.Getter double getStartX();
        @Reflection.Getter double getStartY();

        @Reflection.Getter List<Stop> getStops();

        @Reflection.Getter("proportional") boolean isProportional();
    }

    @Reflection.Signature
    public void __construct(double startX, double startY, double endX, double endY, boolean proportional, CycleMethod cycleMethod, List<Stop> stops){
        __wrappedObject = new LinearGradient(startX, startY, endX, endY, proportional, cycleMethod, stops);
    }

    @Reflection.Signature
    public static LinearGradient of(String value){
        try{
            return LinearGradient.valueOf(value);
        }
        catch(IllegalArgumentException e){
            return null;
        }
    }
}