package org.develnext.jphp.ext.javafx.classes.text;

import javafx.scene.text.Font;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

import java.io.InputStream;
import java.util.List;

@Reflection.Name(JavaFXExtension.NAMESPACE + "text\\UXFont")
public class UXFont extends BaseWrapper<Font> {
    interface WrappedInterface {
        @Property String name();
        @Property String family();
        @Property String size();
        @Property String style();
    }

    public UXFont(Environment env, Font wrappedObject) {
        super(env, wrappedObject);
    }

    public UXFont(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct(double size) {
        __wrappedObject = new Font(size);
    }

    @Signature
    public void __construct(double size, String name) {
        __wrappedObject = new Font(name, size);
    }

    @Signature
    public static List<String> getFamilies() {
        return Font.getFamilies();
    }

    @Signature
    public static List<String> getFontNames() {
        return Font.getFontNames();
    }

    @Signature
    public static List<String> getFontNames(String family) {
        return Font.getFontNames(family);
    }

    @Signature
    public static Font getDefault() {
        return Font.getDefault();
    }

    @Signature
    public static Font of(String family, int size) {
        return Font.font(family, size);
    }

    @Signature
    public static Font load(InputStream stream, double size) {
        return Font.loadFont(stream, size);
    }
}
