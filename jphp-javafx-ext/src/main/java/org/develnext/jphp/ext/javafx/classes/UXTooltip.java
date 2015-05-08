package org.develnext.jphp.ext.javafx.classes;

import javafx.scene.Node;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Name(JavaFXExtension.NAMESPACE + "UXTooltip")
public class UXTooltip<T extends Tooltip> extends UXPopupWindow<Tooltip> {
    interface WrappedInterface {
        @Property Font font();
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

    @Signature
    public void __construct() {
        __wrappedObject = new Tooltip();
    }

    @Signature
    public void __construct(String text) {
        __wrappedObject = new Tooltip(text);
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
