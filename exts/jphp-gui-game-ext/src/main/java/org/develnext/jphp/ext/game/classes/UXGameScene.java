package org.develnext.jphp.ext.game.classes;

import org.develnext.jphp.ext.game.GameExtension;
import org.develnext.jphp.ext.game.support.GameEntity2D;
import org.develnext.jphp.ext.game.support.GameScene2D;
import org.develnext.jphp.ext.game.support.Vec2d;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Nullable;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

@Reflection.Namespace(GameExtension.NS)
public class UXGameScene extends BaseWrapper<GameScene2D> {
    interface WrappedInterface {
        @Property Vec2d gravity();
        @Property double gravityX();
        @Property double gravityY();
        @Property @Nullable GameEntity2D observedObject();

        void play();
        void pause();
        void clear();
    }

    public UXGameScene(Environment env, GameScene2D wrappedObject) {
        super(env, wrappedObject);
    }

    public UXGameScene(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new GameScene2D();
    }

    @Signature
    public void add(GameEntity2D entity) {
        getWrappedObject().addEntity(entity);
    }

    @Signature
    public void remove(GameEntity2D entity) {
        getWrappedObject().removeEntity(entity);
    }

    @Signature
    public void setScrollHandler(@Nullable final Invoker invoker) {
        if (invoker == null) {
            getWrappedObject().setScrollHandler(null);
        } else {
            getWrappedObject().setScrollHandler(new GameScene2D.ScrollHandler() {
                @Override
                public void scrollTo(double x, double y) {
                    invoker.callAny(x, y);
                }
            });
        }
    }
}
