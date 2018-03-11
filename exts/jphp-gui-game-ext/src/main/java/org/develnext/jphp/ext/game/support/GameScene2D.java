package org.develnext.jphp.ext.game.support;

import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.dyn4j.dynamics.*;
import org.dyn4j.dynamics.contact.Contact;
import org.dyn4j.dynamics.contact.ContactConstraint;
import org.dyn4j.geometry.AABB;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Transform;
import org.dyn4j.geometry.Vector2;

import java.util.ArrayList;
import java.util.List;

public class GameScene2D {
    public interface ScrollHandler {
        void scrollTo(double x, double y);
    }

    protected World world;

    private static final float TIME_STEP = 1 / 60.0f;

    private AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long internalTime) {
            processUpdate(System.nanoTime());
        }
    };

    protected List<GameEntity2D> entities = new ArrayList<>();

    protected Vec2d gravity = new Vec2d(0, 0);

    protected DoubleProperty width = new SimpleDoubleProperty(0);
    protected DoubleProperty height = new SimpleDoubleProperty(0);

    protected ObjectProperty<GameEntity2D> observedObject = new SimpleObjectProperty<>(null);
    protected ObjectProperty<ScrollHandler> scrollHandler = new SimpleObjectProperty<>(null);

    public GameScene2D() {
        world = new World(Capacity.DEFAULT_CAPACITY);

        world.addListener(new CollisionAdapter() {
            @Override
            public boolean collision(ContactConstraint contact) {
                return triggerCollision(contact);
            }
        });
    }

    public boolean triggerCollision(ContactConstraint contact) {
        GameEntity2D e1 = (GameEntity2D) contact.getBody1().getUserData();
        GameEntity2D e2 = (GameEntity2D) contact.getBody2().getUserData();

        boolean consume1 = !contact.isSensor() && e1.triggerCollision(e2, contact, true);
        boolean consume2 = !contact.isSensor() && e2.triggerCollision(e1, contact, false);

        return consume1 || consume2;
    }

    public List<DetectResult> detectCollision(GameEntity2D entity2D, double x, double y) {
        ArrayList<DetectResult> results = new ArrayList<>();

        List<BodyFixture> fixtures = entity2D.body.getFixtures();
        Transform transform = new Transform();
        transform.setTranslation(x + entity2D.center.x, y + entity2D.center.y);

        for (BodyFixture fixture : fixtures) {
            world.detect(fixture.getShape(), transform, null, false, true, true, results);
        }

        return results;
    }

    public void play() {
        timer.start();
    }

    public void pause() {
        timer.stop();
    }

    public ObjectProperty<GameEntity2D> observedObjectProperty() {
        return observedObject;
    }

    public DoubleProperty widthProperty() {
        return width;
    }

    public DoubleProperty heightProperty() {
        return height;
    }

    public GameEntity2D getObservedObject() {
        return observedObject.get();
    }

    public void setObservedObject(GameEntity2D observedObject) {
        this.observedObject.set(observedObject);
    }

    public ScrollHandler getScrollHandler() {
        return scrollHandler.get();
    }

    public ObjectProperty<ScrollHandler> scrollHandlerProperty() {
        return scrollHandler;
    }

    public void setScrollHandler(ScrollHandler scrollHandler) {
        this.scrollHandler.set(scrollHandler);
    }

    public void setWidth(double v) {
        widthProperty().set(v);
    }

    public void setHeight(double v) {
        heightProperty().set(v);
    }

    public double getWidth() {
        return widthProperty().get();
    }

    public double getHeight() {
        return heightProperty().get();
    }

    public static float toMeters(double pixels) {
        return (float)pixels * 0.005f;
    }

    public static float toPixels(double meters) {
        return (float)meters * 20f;
    }

    private long previousTime = 0;

    private void processUpdate(long internalTime) {
        if (previousTime == 0) {
            previousTime = internalTime;
            return;
        }

        //float delta = ((internalTime - previousTime) / 1000.0f / 1000.0f / 1000.0f);

        previousTime = internalTime;

        world.update(TIME_STEP);

        for (GameEntity2D entity : entities) {
            entity.update(TIME_STEP, this);

            GameEntity2D entity2D = observedObject.get();

            if (entity2D != null) {
                ScrollHandler scrollHandler = this.scrollHandler.get();

                if (scrollHandler != null) {
                    scrollHandler.scrollTo(entity.getCenterX(), entity.getCenterY());
                }
            }
        }
    }

    public void addEntity(GameEntity2D entity) {
        if (entity.getScene() == null) {
            entity.scene = this;

            entities.add(entity);
            world.addBody(entity.body);

            entity.onSceneAdd();
        }
    }

    public void removeEntity(GameEntity2D entity) {
        entities.remove(entity);
        world.removeBody(entity.body);
        entity.scene = null;
    }

    public void clear() {
        world.removeAllBodies();

        for (GameEntity2D entity : entities) {
            entity.scene = null;
        }

        entities.clear();
    }

    public Vec2d getGravity() {
        return gravity;
    }

    public void setGravity(Vec2d gravity) {
        this.gravity = gravity;
        this.world.setGravity(new Vector2(gravity.x, -gravity.y * 10));
    }

    public double getGravityX() {
        return gravity.x;
    }

    public void setGravityX(double x) {
        gravity.x = x;
        world.setGravity(new Vector2(gravity.x, -gravity.y * 10));
    }

    public double getGravityY() {
        return gravity.y;
    }

    public void setGravityY(double y) {
        gravity.y = y;
        world.setGravity(new Vector2(gravity.x, -gravity.y * 10));
    }
}
