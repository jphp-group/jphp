package org.develnext.jphp.ext.game.support;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;
import org.dyn4j.dynamics.contact.ContactConstraint;
import org.dyn4j.geometry.Vector2;

public class CollisionEvent extends Event {
    public static final EventType<CollisionEvent> COLLISION =
            new EventType<>(Event.ANY, "COLLISION");

    protected Vec2d normal = new Vec2d(0, 0);
    protected Vec2d tangent = new Vec2d(0, 0);

    public CollisionEvent() {
        super(COLLISION);
    }

    public CollisionEvent(Object source, EventTarget target) {
        super(source, target, COLLISION);
    }

    public CollisionEvent(GameEntity2D gameEntity2D, GameEntity2D other, ContactConstraint constraint) {
        super(gameEntity2D, other, COLLISION);

        Vector2 normal = constraint.getNormal();
        Vector2 tangent = constraint.getTangent();

        this.normal = new Vec2d(normal.x, normal.y);
        this.tangent = new Vec2d(tangent.x, tangent.y);
    }

    public CollisionEvent(CollisionEvent parent, Object sender, EventTarget target) {
        super(sender, target, COLLISION);
        normal = parent.normal;
        tangent = parent.normal;
    }

    public Vec2d getNormal() {
        return normal;
    }

    public Vec2d getTangent() {
        return tangent;
    }
}
