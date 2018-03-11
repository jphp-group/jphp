package org.develnext.jphp.ext.game.classes;

import org.develnext.jphp.ext.game.GameExtension;
import org.develnext.jphp.ext.game.support.Sprite;
import org.develnext.jphp.ext.game.support.SpriteView;
import org.develnext.jphp.ext.javafx.classes.UXCanvas;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Nullable;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Reflection.Namespace(GameExtension.NS)
public class UXSpriteView extends UXCanvas<SpriteView> {
    interface WrappedInterface {
        @Property @Nullable Sprite sprite();
        @Property("animated") boolean animationEnabled();
        @Property String animationName();
        @Property int animationSpeed();

        @Property int frame();
        @Property int frameCount();

        @Property boolean flipX();
        @Property boolean flipY();

        void play(String animation);
        void play(String animation, int speed);
        void playOnce(String animation);
        void playOnce(String animation, int speed);

        void pause();
        boolean isPaused();
        void resume();

        void update(long now);
    }

    public UXSpriteView(Environment env, SpriteView wrappedObject) {
        super(env, wrappedObject);
    }

    public UXSpriteView(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new SpriteView();
    }

    @Signature
    public void __construct(@Nullable Sprite sprite) {
        __wrappedObject = new SpriteView(sprite);
    }
}
