package org.develnext.jphp.ext.game.classes;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import org.develnext.jphp.ext.game.GameExtension;
import org.develnext.jphp.ext.game.support.Sprite;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

@Namespace(GameExtension.NS)
public class UXSprite extends BaseWrapper<Sprite> {
    interface WrappedInterface {
        @Property double[] frameSize();
        @Property double frameWidth();
        @Property double frameHeight();
        @Property int speed();
        @Property Image image();
        @Property boolean cycledAnimation();
        @Property @Nullable String currentAnimation();

        void setAnimation(String name, int... indexes);
        void setAnimationSpeed(String name, int speed);

        void draw(Canvas node, int index);
        void drawByTime(Canvas canvas, long now);
        void drawNext(Canvas canvas);
        Image getFrameImage(int index);

        void freeze();
        void unfreeze();
        boolean isFreeze();

        void play(String animation);
        void play(String animation, int speed);
    }

    public UXSprite(Environment env, Sprite wrappedObject) {
        super(env, wrappedObject);
    }

    public UXSprite(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __construct(null);
    }

    @Signature
    public void __construct(@Nullable Sprite sprite) {
        __wrappedObject = new Sprite(sprite);
    }

    @Getter
    public int getFrameCount() {
        return getWrappedObject().getMaxIndex() + 1;
    }
}
