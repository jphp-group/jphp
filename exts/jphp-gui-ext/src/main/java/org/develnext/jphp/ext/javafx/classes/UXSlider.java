package org.develnext.jphp.ext.javafx.classes;

import javafx.geometry.Orientation;
import javafx.scene.control.Slider;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Reflection.Name(JavaFXExtension.NS + "UXSlider")
public class UXSlider extends UXControl<Slider> {
    public UXSlider(Environment env, Slider wrappedObject) {
        super(env, wrappedObject);
    }

    public UXSlider(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    interface WrappedInterface {
        @Property Orientation orientation();
        @Property boolean showTickMarks();
        @Property boolean showTickLabels();
        @Property boolean snapToTicks();

        @Property double blockIncrement();
        @Property int minorTickCount();
        @Property double majorTickUnit();

        @Property double min();
        @Property double max();
        @Property double value();
    }

    @Signature
    public void __construct() {
        __wrappedObject = new Slider();
    }
}
