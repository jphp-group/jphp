package org.develnext.jphp.ext.javafx.classes.paint;

import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

import java.util.List;

@Reflection.Name(JavaFXExtension.NS + "paint\\UXRadialGradient")
public class UXRadialGradient extends UXPaint<RadialGradient>{

    public UXRadialGradient(Environment env, RadialGradient wrappedObject) {
        super(env, wrappedObject);
    }

    public UXRadialGradient(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    interface WrappedInterface{
        @Reflection.Getter CycleMethod getCycleMethod();

        @Reflection.Getter double getCenterX();
        @Reflection.Getter double getCenterY();

        @Reflection.Getter double getFocusAngle();
        @Reflection.Getter double getFocusDistance();
        @Reflection.Getter double getRadius();

        @Reflection.Getter List<Stop> getStops();

        @Reflection.Getter("proportional") boolean isProportional();
    }

    @Reflection.Signature
    public void __construct(double focusAngle, double focusDistance, double centerX, double centerY, double radius, boolean proportional, CycleMethod cycleMethod, List<Stop> stops){
        __wrappedObject = new RadialGradient(focusAngle, focusDistance, centerX, centerY, radius, proportional, cycleMethod, stops);
    }

    @Reflection.Signature
    public static RadialGradient of(String value){
        try{
            return RadialGradient.valueOf(value);
        }
        catch(IllegalArgumentException e){
            return null;
        }
    }
}