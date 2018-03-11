package org.develnext.jphp.ext.javafx.classes.layout;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.layout.TilePane;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Reflection.Name(JavaFXExtension.NS + "layout\\UXTilePane")
public class UXTilePane extends UXPane<TilePane> {
    interface WrappedInterface {
        @Property double hgap();
        @Property double vgap();

        @Property Pos alignment();
        @Property Pos tileAlignment();

        @Property double prefTileWidth();
        @Property double prefTileHeight();

        @Property int prefColumns();
        @Property int prefRows();

        @Property Orientation orientation();

    }

    public UXTilePane(Environment env, TilePane wrappedObject) {
        super(env, wrappedObject);
    }

    public UXTilePane(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new TilePane();
    }
}
