package org.develnext.jphp.ext.game.support;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.EventDispatchChain;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import org.dyn4j.collision.manifold.Manifold;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.DetectResult;
import org.dyn4j.dynamics.contact.ContactConstraint;
import org.dyn4j.geometry.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameEntity2D implements EventTarget {
    public enum BodyType {STATIC, DYNAMIC, KINEMATIC}
    public enum SolidType {NONE, PLATFORM, MATERIAL, THING};

    private static final float TIME = 1 / 60.0f;

    Body body;

    protected BodyType bodyType = BodyType.STATIC;
    protected SolidType solidType = SolidType.NONE;

    protected Vec2d velocity = new Vec2d(0, 0);
    protected Vec2d gravity = null;

    private final String entityType;
    private final Node node;

    protected double width = -1;
    protected double height = -1;
    protected Vector2 center = new Vector2(0, 0);

    protected boolean gravityFreeze = false;

    protected DoubleProperty x = new SimpleDoubleProperty(0);
    protected DoubleProperty y = new SimpleDoubleProperty(0);
    protected DoubleProperty rotation = new SimpleDoubleProperty(0);

    private Double direction;

    GameScene2D scene;

    protected Map<String, EventHandler<CollisionEvent>> collisionHandlers = new HashMap<>();

    public GameEntity2D(String entityType, final Node node) {
        this.entityType = entityType;
        this.node = node;

        this.body = new Body();
        this.body.setUserData(this);

        this.body.setMass(MassType.NORMAL);

        this.body.addFixture(new Rectangle(getWidth(), getHeight()));
        this.body.setActive(!node.isDisabled());

        xProperty().addListener((observable, oldValue, newValue) -> {
            updateBodyPos();
        });

        yProperty().addListener((observable, oldValue, newValue) -> {
            updateBodyPos();
        });

        rotationProperty().addListener((observable, oldValue, newValue) -> {
            node.setRotate(newValue.doubleValue());
        });

        setX(node.getLayoutX());
        setY(node.getLayoutY());
        setRotation(node.getRotate());

        ChangeListener<Number> verticalCoordListener = getVerticalCoordListener(node);
        ChangeListener<Number> horizontalCoordListener = getHorizontalCoordListener(node);

        node.layoutYProperty().addListener(verticalCoordListener);
        node.layoutXProperty().addListener(horizontalCoordListener);

        node.layoutXProperty().bindBidirectional(x);
        node.layoutYProperty().bindBidirectional(y);
        //node.rotateProperty().bindBidirectional(rotation);

        ChangeListener<Number> activeTrigger = (observable, oldValue, newValue) -> this.body.setActive(true);
        node.layoutYProperty().addListener(activeTrigger);
        node.layoutYProperty().addListener(activeTrigger);

        node.disabledProperty().addListener((observable, oldValue, newValue) -> {
            GameEntity2D.this.body.setActive(!newValue);
        });
    }

    private boolean horizontalCoordListenerLock = false;

    private ChangeListener<Number> getHorizontalCoordListener(Node node) {
        return (observable, oldValue, newValue) -> {
            if (scene == null || node == null) return;

            if (isSolid() && !node.isDisabled() && solidType == SolidType.THING && !horizontalCoordListenerLock) {
                List<DetectResult> results = scene.detectCollision(GameEntity2D.this, newValue.doubleValue(), node.getLayoutY());

                horizontalCoordListenerLock = true;

                try {
                    for (DetectResult result : results) {
                        GameEntity2D entity = (GameEntity2D) result.getBody().getUserData();

                        if (entity != GameEntity2D.this && entity.isSolid()) {
                            Vector2 normal = result.getPenetration().getNormal();
                            double depth = result.getPenetration().getDepth();

                            if (entity.isPlatform()) {
                                continue;
                            }

                            double xValue = newValue.doubleValue() - normal.x * depth;
                            double yValue = node.getLayoutY() - normal.y * depth;

                            node.setLayoutX(xValue);
                            setX(xValue);

                            node.setLayoutY(yValue);
                            setY(yValue);

                            setVelocityX(0);

                            if (scene != null) {
                                ContactConstraint contact = new ContactConstraint(
                                        body, body.getFixture(0), result.getBody(), result.getFixture(), new Manifold(Collections.emptyList(), normal.getNegative()), 1.0, 1.0
                                );

                                scene.triggerCollision(contact);
                            }

                            return;
                        }
                    }
                } finally {
                    horizontalCoordListenerLock = false;
                }
            }
        };
    }

    private boolean verticalCoordListenerLock = false;

    private ChangeListener<Number> getVerticalCoordListener(Node node) {
        return (observable, oldValue, newValue) -> {
            if (scene == null || node == null) return;

            if (isSolid() && !node.isDisabled() && solidType == SolidType.THING && !verticalCoordListenerLock) {
                List<DetectResult> results = scene.detectCollision(GameEntity2D.this, node.getLayoutX(), newValue.doubleValue());

                verticalCoordListenerLock = true;

                try {
                    for (DetectResult result : results) {
                        GameEntity2D entity = (GameEntity2D) result.getBody().getUserData();

                        if (entity != GameEntity2D.this && entity.isSolid()) {
                            Vector2 normal = result.getPenetration().getNormal();
                            double depth = result.getPenetration().getDepth();

                            if (entity.isPlatform()) {
                                if (newValue.doubleValue() <= oldValue.doubleValue() || depth >= 8 || normal.y < 0) {
                                    continue;
                                }
                            }

                            double yValue = newValue.doubleValue() - normal.y * depth;
                            double xValue = node.getLayoutX() - normal.x * depth;

                            node.setLayoutY(yValue);
                            setY(yValue);

                            if (!entity.isPlatform()) {
                                node.setLayoutX(xValue);
                                setX(xValue);
                            }

                            setVelocityY(0);

                            if (scene != null) {
                                ContactConstraint contact = new ContactConstraint(
                                        body, body.getFixture(0), result.getBody(), result.getFixture(), new Manifold(Collections.emptyList(), normal.getNegative()), 1.0, 1.0
                                );

                                scene.triggerCollision(contact);
                            }

                            return;
                        }
                    }
                } finally {
                    verticalCoordListenerLock = false;
                }
            }
        };
    }

    public void setPolygonFixture(Vec2d[] points) {
        this.body.removeAllFixtures();

        Vector2[] _points = new Vector2[points.length];

        for (int i = 0; i < points.length; i++) {
            _points[i] = new Vector2(points[i].x, points[i].y);
        }

        if (_points.length == 2) {
            this.body.addFixture(new Segment(_points[0], _points[1]));
        } else if (_points.length == 3) {
            this.body.addFixture(new Triangle(_points[0], _points[1], _points[2]));
        } else {
            this.body.addFixture(new Polygon(_points));
        }
    }

    public boolean getActive() {
        return body.isActive();
    }

    public void setActive(boolean value) {
        body.setActive(value);
    }

    public void setCircleFixture(double radius) {
        this.body.removeAllFixtures();
        this.width = this.height = radius * 2;
        Circle circle = new Circle(radius);
        this.center = new Vector2(radius, radius);

        this.body.addFixture(circle);
    }

    public void setEllipseFixture(double width, double height) {
        this.body.removeAllFixtures();
        this.width = width;
        this.height = height;
        Ellipse ellipse = new Ellipse(width, height);

        this.center = new Vector2(width / 2, height / 2);

        this.body.addFixture(ellipse);
    }

    public void setRectangleFixture(double width, double height) {
        this.body.removeAllFixtures();
        this.width = width;
        this.height = height;
        Rectangle rectangle = new Rectangle(width, height);

        this.center = new Vector2(width / 2, height / 2);

        this.body.addFixture(rectangle);
    }

    void onSceneAdd() {
        updateBodyPos();
    }

    protected void updateBodyPos() {
        if (scene == null) {
            return;
        }

        body.getTransform().setTranslation(getCenterX(), getCenterY());
    }

    public DoubleProperty xProperty() {
        return x;
    }

    public DoubleProperty yProperty() {
        return y;
    }

    public void setX(double v) {
        xProperty().set(v);
    }

    public void setY(double y) {
        yProperty().set(y);
    }

    public double getX() {
        return xProperty().get();
    }

    public double getY() {
        return yProperty().get();
    }

    public double getCenterX() {
        return getX() + center.x;
    }

    public double getCenterY() {
        return getY() + center.y;
    }

    public void setCenterX(double v) {
        setX(v - getWidth() / 2);
    }

    public void setCenterY(double v) {
        setY(v - getHeight() / 2);
    }

    public Vec2d getVelocity() {
        return velocity;
    }

    public void setVelocity(Vec2d velocity) {
        this.velocity = velocity == null ? new Vec2d(0, 0) : velocity;
    }

    public double getHorizontalVelocity() {
        return velocity.x;
    }

    public double getVerticalVelocity() {
        return velocity.y;
    }

    public void setHorizontalVelocity(double value) {
        this.velocity = new Vec2d(value, velocity.y);
    }

    public void setVerticalVelocity(double value) {
        this.velocity = new Vec2d(velocity.x, value);
    }

    public String getEntityType() {
        return entityType;
    }

    public Node getNode() {
        return node;
    }

    public double getWidth() {
        if (width >= 0) return width;

        Bounds bounds = node.getBoundsInParent();
        double width = bounds.getWidth();
        return width <= 0 ? 1 : width;
    }

    public double getHeight() {
        if (height >= 0) return height;

        Bounds bounds = node.getBoundsInParent();
        double height = bounds.getHeight();
        return height <= 0 ? 1 : height;
    }

    void update(float dt, GameScene2D scene) {
        switch (bodyType) {
            case DYNAMIC:
            case KINEMATIC:
                if (body.isActive()) {
                    Vec2d gravity = this.gravity;

                    if (gravity == null) {
                        gravity = scene.gravity;
                    }

                    if (gravity != null) {
                        velocity.x += gravity.x * dt;
                        velocity.y += gravity.y * dt;
                    }

                    if (velocity.x > 0.00001 || velocity.x < -00000.1) {
                        x.set(x.get() + GameScene2D.toPixels(velocity.x * dt));
                    }

                    if (velocity.y > 0.00001 || velocity.y < -00000.1) {
                        y.set(y.get() + GameScene2D.toPixels(velocity.y * dt));
                    }

                    // node.setLayoutX(x.get());
                    // node.setLayoutY(y.get());
                }

                break;

            case STATIC:
            default:
        }

    }

    public GameScene2D getScene() {
        return scene;
    }

    public BodyType getBodyType() {
        return bodyType;
    }

    public void setBodyType(BodyType bodyType) {
        this.bodyType = bodyType;
    }

    public Vec2d getGravity() {
        return gravity;
    }

    public void setGravity(Vec2d gravity) {
        this.gravity = gravity;
    }

    public double getGravityX() {
        return gravity == null ? 0.0 : gravity.x;
    }

    public void setGravityX(double x) {
        if (gravity == null) {
            gravity = new Vec2d(x, 0.0);
        } else {
            gravity.x = x;
        }
    }

    public double getGravityY() {
        return gravity.y;
    }

    public void setGravityY(double y) {
        if (gravity == null) {
            gravity = new Vec2d(0.0, y);
        } else {
            gravity.y = y;
        }
    }

    public double getHspeed() {
        return getVelocityX();
    }

    public double getVspeed() {
        return getVelocityY();
    }

    public void setHspeed(double value) {
        setVelocityX(value);
    }

    public void setVspeed(double value) {
        setVelocityY(value);
    }

    public double getVelocityX() {
        return velocity.x;
    }

    public double getVelocityY() {
        return velocity.y;
    }

    public void setVelocityX(double value) {
        velocity.x = value;

        body.setActive(true);
    }

    public void setVelocityY(double value) {
        velocity.y = value;

        body.setActive(true);
    }

    public void setAngleSpeed(Vec2d speed) {
        double direction = -Math.toRadians(speed.x);
        velocity = new Vec2d(speed.y * Math.cos(direction), speed.y * Math.sin(direction));
        body.setActive(true);
    }

    public Vec2d getAngleSpeed() {
        return new Vec2d(getDirection(), getSpeed());
    }

    public double getSpeed() {
        return velocity.length();
    }

    public double getDirection() {
        if (this.direction != null) {
            return this.direction;
        }

        return 360 - Math.toDegrees(Math.atan2(velocity.y, velocity.x));
    }

    public void setDirection(double value) {
        if (getSpeed() == 0.0) {
            direction = value;
        } else {
            value = -Math.toRadians(value);

            double speed = getSpeed();
            velocity = new Vec2d(speed * Math.cos(value), speed * Math.sin(value));
        }

        body.setActive(true);
    }

    public void setSpeed(double value) {
        double oldDirection = getDirection();

        if (this.direction != null) {
            setAngleSpeed(new Vec2d(this.direction, value));
            this.direction = null;
        } else {
            setAngleSpeed(new Vec2d(getDirection(), value));
        }

        if (value == 0.0) {
            direction = oldDirection;
        }

        body.setActive(true);
    }

    public boolean triggerCollision(GameEntity2D other, ContactConstraint constraint, boolean negative) {
        EventHandler<CollisionEvent> eventHandler = collisionHandlers.get(other.getEntityType());

        if (eventHandler != null) {
            CollisionEvent event = new CollisionEvent(this, other, constraint);
            if (negative) {
                event.normal.negateLocal();
            }

            eventHandler.handle(event);

            return event.isConsumed();
        }

        return false;
    }

    public void setCollisionHandler(String entityType, EventHandler<CollisionEvent> collisionHandler) {
        collisionHandlers.put(entityType, collisionHandler);
    }

    @Override
    public EventDispatchChain buildEventDispatchChain(EventDispatchChain tail) {
        return null;
    }

    public boolean isSolid() {
        return solidType != SolidType.NONE;
    }

    public boolean isPlatform() {
        return solidType == SolidType.PLATFORM;
    }

    public double getRotation() {
        return rotation.get();
    }

    public DoubleProperty rotationProperty() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation.set(rotation);
    }

    public boolean isGravityFreeze() {
        return gravityFreeze;
    }

    public void setGravityFreeze(boolean gravityFreeze) {
        this.gravityFreeze = gravityFreeze;
    }

    public SolidType getSolidType() {
        return solidType;
    }

    public void setSolidType(SolidType solidType) {
        this.solidType = solidType;
    }

    public boolean isPlaceFree(double x, double y) {
        if (scene != null) {
            return scene.detectCollision(this, x, y).isEmpty();
        }

        return true;
    }
}
