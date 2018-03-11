package org.develnext.jphp.ext.game.classes;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import org.develnext.jphp.ext.game.GameExtension;
import org.develnext.jphp.ext.game.support.GameBackground;
import org.develnext.jphp.ext.game.support.GamePane;
import org.develnext.jphp.ext.game.support.Vec2d;
import org.develnext.jphp.ext.javafx.classes.UXCanvas;
import org.develnext.jphp.ext.javafx.classes.layout.UXPanel;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Nullable;
import php.runtime.annotation.Reflection.Property;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Namespace(GameExtension.NS)
public class UXGameBackground extends UXCanvas<GameBackground> {
    interface WrappedInterface {
        @Property @Nullable Image image();
        @Property Vec2d velocity();
        @Property("viewPosition") Vec2d viewPos();

        @Property boolean autoSize();
    }

    public UXGameBackground(Environment env, GameBackground wrappedObject) {
        super(env, wrappedObject);
    }

    public UXGameBackground(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public void __construct() {
        __wrappedObject = new GameBackground();
    }

    public GameBackground getWrappedObject() {
        return (GameBackground) super.getWrappedObject();
    }
}
