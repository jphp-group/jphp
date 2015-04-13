package org.develnext.jphp.ext.libgdx.classes.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import org.develnext.jphp.ext.libgdx.LibGDXExtension;
import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

@Reflection.Name(LibGDXExtension.NAMESPACE + "graphics\\Batch")
@Reflection.WrapInterface(value = Batch.class, skipConflicts = true)
public class WrapBatch extends BaseWrapper<Batch> {
    public WrapBatch(Environment env, Batch wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapBatch(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }
}
