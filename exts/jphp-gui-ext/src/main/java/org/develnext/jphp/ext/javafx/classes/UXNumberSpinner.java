package org.develnext.jphp.ext.javafx.classes;

import javafx.geometry.Pos;
import javafx.scene.control.SpinnerValueFactory;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import org.develnext.jphp.ext.javafx.support.control.NumberSpinner;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Getter;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Setter;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Reflection.Name(JavaFXExtension.NS + "UXNumberSpinner")
public class UXNumberSpinner extends UXSpinner<NumberSpinner> {
    interface WrappedInterface {
        @Property int min();
        @Property int max();
        @Property int step();
        @Property int initial();
        @Property NumberSpinner.ArrowsStyle arrowsStyle();
    }

    public UXNumberSpinner(Environment env, NumberSpinner wrappedObject) {
        super(env, wrappedObject);
    }

    public UXNumberSpinner(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public void __construct() {
        __wrappedObject = new NumberSpinner();
    }

    @Getter
    public int getValue() {
        return getWrappedObject().getValue();
    }

    @Setter
    public void setValue(int value) {
        getWrappedObject().getValueFactory().setValue(value);
    }

    @Setter
    public void setAlignment(Pos value) {
        getWrappedObject().getEditor().setAlignment(value);
    }

    @Getter
    public Pos getAlignment() {
        return getWrappedObject().getEditor().getAlignment();
    }
}
