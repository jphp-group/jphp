package org.develnext.jphp.ext.game.classes;

import javafx.event.EventHandler;
import javafx.scene.Node;
import org.develnext.jphp.ext.game.GameExtension;
import org.develnext.jphp.ext.game.support.CollisionEvent;
import org.develnext.jphp.ext.game.support.GameEntity2D;
import org.develnext.jphp.ext.game.support.Vec2d;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Environment;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

@Namespace(GameExtension.NS)
public class UXGameEntity extends BaseWrapper<GameEntity2D> {
    interface WrappedInterface {
        @Property Node node();
        @Property GameEntity2D.BodyType bodyType();
        @Property @Nullable Vec2d gravity();

        @Property GameEntity2D.SolidType solidType();
        @Property boolean solid();
        @Property boolean platform();
        @Property boolean active();

        @Property double gravityX();
        @Property double gravityY();

        @Property double velocityX();
        @Property double velocityY();

        @Property Vec2d velocity();

        @Property Vec2d angleSpeed();
        @Property double hspeed();
        @Property double vspeed();
        @Property double speed();
        @Property double direction();

        void setPolygonFixture(Vec2d[] points);
        void setRectangleFixture(double width, double height);
        void setEllipseFixture(double width, double height);
        void setCircleFixture(double radius);

        boolean isPlaceFree(double x, double y);
    }

    public UXGameEntity(Environment env, GameEntity2D wrappedObject) {
        super(env, wrappedObject);
    }

    public UXGameEntity(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct(String entityType, Node node) {
        __wrappedObject = new GameEntity2D(entityType, node);
    }

    @Getter
    public UXGameScene getGameScene(Environment env) {
        return getWrappedObject().getScene() == null ? null : new UXGameScene(env, getWrappedObject().getScene());
    }

    @Signature
    public void setCollisionHandler(String entityType, @Nullable final Invoker handler) {
        if (handler == null) {
            getWrappedObject().setCollisionHandler(entityType, null);
        } else {
            getWrappedObject().setCollisionHandler(entityType, new EventHandler<CollisionEvent>() {
                @Override
                public void handle(CollisionEvent event) {
                    handler.callAny(event);
                }
            });
        }
    }
}
