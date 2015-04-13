package org.develnext.jphp.ext.libgdx.classes.math;

import com.badlogic.gdx.math.Vector2;
import org.develnext.jphp.ext.libgdx.LibGDXExtension;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.lang.BaseWrapper;
import php.runtime.lang.support.ICloneableObject;
import php.runtime.memory.ArrayMemory;
import php.runtime.reflection.ClassEntity;

import static php.runtime.annotation.Reflection.*;

@Name(LibGDXExtension.NAMESPACE + "math\\Vector2")
@WrapInterface(value = WrapVector2.Methods.class)
public class WrapVector2 extends BaseWrapper<Vector2> implements ICloneableObject<WrapVector2> {
    public WrapVector2(Environment env, Vector2 wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapVector2(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new Vector2();
    }

    @Signature
    public void __construct(float x, float y) {
        __wrappedObject = new Vector2(x, y);
    }

    @Signature
    public void __construct(Vector2 vector2) {
        __wrappedObject = new Vector2(vector2);
    }

    @Signature
    public Memory __debugInfo(Environment env, Memory... arg) {
        ArrayMemory result = new ArrayMemory();
        result.refOfIndex("*x").assign(__wrappedObject.x);
        result.refOfIndex("*y").assign(__wrappedObject.y);

        return result.toConstant();
    }

    @Signature
    public float x() {
        return __wrappedObject.x;
    }

    @Signature
    public float y() {
        return __wrappedObject.y;
    }

    @Override
    public WrapVector2 __clone(Environment environment, TraceInfo traceInfo) {
        return new WrapVector2(environment, __wrappedObject.cpy());
    }

    interface Methods {
        float len ();
        float len2 ();

        Vector2 set (Vector2 v);
        Vector2 set (float x, float y);

        Vector2 sub (Vector2 v);
        Vector2 sub (float x, float y);

        Vector2 nor ();

        Vector2 add (Vector2 v);
        Vector2 add (float x, float y);

        float dot (Vector2 v);
        float dot (float ox, float oy);

        Vector2 scl (float scalar);
        Vector2 scl (float x, float y);

        Vector2 mulAdd (Vector2 vec, float scalar);

        float dst (Vector2 v);
        float dst (float x, float y);

        float dst2 (Vector2 v);
        float dst2 (float x, float y);

        Vector2 limit (float limit);
        Vector2 clamp (float min, float max);

        float crs (Vector2 v);
        float crs (float x, float y);

        float angle ();
        float getAngleRad ();

        Vector2 setAngle (float degrees);
        Vector2 setAngleRad (float radians);

        Vector2 rotate (float degrees);
        Vector2 rotateRad (float radians);
        Vector2 rotate90 (int dir);

        Vector2 lerp (Vector2 target, float alpha);

        boolean epsilonEquals (Vector2 other, float epsilon);

        boolean isUnit ();
        boolean isUnit (final float margin);
        boolean isZero ();
        boolean isZero (final float margin);
        boolean isOnLine (Vector2 other);
        boolean isOnLine (Vector2 other, float epsilon);
        boolean isCollinear (Vector2 other, float epsilon);
        boolean isCollinear (Vector2 other);
        boolean isCollinearOpposite (Vector2 other, float epsilon);
        boolean isCollinearOpposite (Vector2 other);
        boolean isPerpendicular (Vector2 vector);
        boolean isPerpendicular (Vector2 vector, float epsilon);
        boolean hasSameDirection (Vector2 vector);
        boolean hasOppositeDirection (Vector2 vector);
    }
}
