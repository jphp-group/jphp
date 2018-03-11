package org.develnext.jphp.ext.javafx.classes;

import javafx.scene.Node;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import org.develnext.jphp.ext.javafx.classes.text.UXFont;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Nullable;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Name(JavaFXExtension.NS + "UXTooltip")
public class UXTooltip<T extends Tooltip> extends UXPopupWindow<Tooltip> {
    interface WrappedInterface {
        @Property Node graphic();
        @Property double graphicTextGap();
        @Property String text();
        @Property TextAlignment textAlignment();
        @Property OverrunStyle textOverrun();

        @Property boolean activated();
        @Property boolean wrapText();
    }

    public UXTooltip(Environment env, T wrappedObject) {
        super(env, wrappedObject);
    }

    public UXTooltip(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getWrappedObject() {
        return (T) super.getWrappedObject();
    }

    @Reflection.Getter
    public UXFont getFont(Environment env) {
        return new UXFont(env, getWrappedObject().getFont(), font -> getWrappedObject().setFont(font));
    }

    @Reflection.Setter
    public void setFont(Font font) {
        getWrappedObject().setFont(font);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new Tooltip();
    }

    @Signature
    public static Tooltip of(String text) {
        return new Tooltip(text);
    }

    @Signature
    public static Tooltip of(String text, @Nullable Node graphic) {
        Tooltip tooltip = new Tooltip(text);
        tooltip.setGraphic(graphic);
        return tooltip;
    }

    @Signature
    public static void install(Node node, Tooltip tooltip) {
        Tooltip.install(node, tooltip);
    }

    @Signature
    public static void uninstall(Node node, Tooltip tooltip) {
        Tooltip.uninstall(node, tooltip);
    }
}
