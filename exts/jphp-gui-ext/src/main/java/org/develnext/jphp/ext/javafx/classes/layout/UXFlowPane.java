package org.develnext.jphp.ext.javafx.classes.layout;

import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.layout.FlowPane;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Reflection.Name(JavaFXExtension.NS + "layout\\UXFlowPane")
public class UXFlowPane<T extends FlowPane> extends UXPane<FlowPane> {
    interface WrappedInterface {
        @Property VPos rowValignment();
        @Property HPos columnHalignment();

        @Property double prefWrapLength();

        @Property double hgap();
        @Property double vgap();

        @Property Pos alignment();

        @Property Orientation orientation();
    }

    public UXFlowPane(Environment env, T wrappedObject) {
        super(env, wrappedObject);
    }

    public UXFlowPane(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new FlowPane();
    }
}
