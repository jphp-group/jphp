package org.develnext.jphp.ext.libgdx.classes.math;

import com.badlogic.gdx.math.Polyline;
import org.develnext.jphp.ext.libgdx.LibGDXExtension;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.memory.ArrayMemory;
import php.runtime.reflection.ClassEntity;

import static php.runtime.annotation.Reflection.*;

@Name(LibGDXExtension.NAMESPACE + "math\\Polyline")
@WrapInterface(value = Polyline.class, skipConflicts = true)
public class WrapPolyline extends BaseWrapper<Polyline> {
    public WrapPolyline(Environment env, Polyline wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapPolyline(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new Polyline();
    }

    @Signature
    public void __construct(float[] vertices) {
        __wrappedObject = new Polyline(vertices);
    }

    @Signature
    public Memory __debugInfo(Environment env, Memory... args) {
        ArrayMemory result = new ArrayMemory();
        for (float v : __wrappedObject.getVertices()) {
            result.add(v);
        }

        return result.toConstant();
    }
}
