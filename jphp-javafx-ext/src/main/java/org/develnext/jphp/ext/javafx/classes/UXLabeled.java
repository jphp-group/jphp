package org.develnext.jphp.ext.javafx.classes;

import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Labeled;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Abstract;
import php.runtime.annotation.Reflection.Property;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Abstract
@Reflection.Name(JavaFXExtension.NAMESPACE + "UXLabeled")
public class UXLabeled extends UXControl {
    interface WrappedInterface {
        @Property Pos alignment();
        @Property ContentDisplay contentDisplay();
        @Property String ellipsisString();
        @Property double graphicTextGap();
        @Property String text();
        @Property TextAlignment textAlignment();
        @Property boolean underline();
        @Property boolean wrapText();

        @Property Font font();
    }

    public UXLabeled(Environment env, Labeled wrappedObject) {
        super(env, wrappedObject);
    }

    public UXLabeled(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public Labeled getWrappedObject() {
        return (Labeled) super.getWrappedObject();
    }
}
