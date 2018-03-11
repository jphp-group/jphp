package org.develnext.jphp.ext.javafx.classes;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Labeled;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import org.develnext.jphp.ext.javafx.classes.text.UXFont;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Abstract
@Reflection.Name(JavaFXExtension.NS + "UXLabeled")
public class UXLabeled<T extends Labeled> extends UXControl<Labeled> {
    interface WrappedInterface {
        @Property
        Pos alignment();

        @Property
        ContentDisplay contentDisplay();

        @Property
        String ellipsisString();

        @Property
        double graphicTextGap();

        @Property
        String text();

        @Property
        TextAlignment textAlignment();

        @Property
        boolean underline();

        @Property
        boolean wrapText();

        @Property
        boolean mnemonicParsing();
    }

    public UXLabeled(Environment env, T wrappedObject) {
        super(env, wrappedObject);
    }

    public UXLabeled(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getWrappedObject() {
        return (T) super.getWrappedObject();
    }

    @Setter
    public void setGraphic(@Nullable Node node) {
        getWrappedObject().setGraphic(node);
    }

    @Getter
    public Node getGraphic() {
        return getWrappedObject().getGraphic();
    }

    @Setter
    public void setTextColor(Color color) {
        getWrappedObject().setTextFill(color);
    }

    @Getter
    public UXFont getFont(Environment env) {
        return new UXFont(env, getWrappedObject().getFont(), font -> getWrappedObject().setFont(font));
    }

    @Setter
    public void setFont(Font font) {
        getWrappedObject().setFont(font);
    }

    @Getter
    public Color getTextColor() {
        Paint textFill = getWrappedObject().getTextFill();

        if (textFill instanceof Color) {
            return (Color) textFill;
        }

        return null;
    }
}
