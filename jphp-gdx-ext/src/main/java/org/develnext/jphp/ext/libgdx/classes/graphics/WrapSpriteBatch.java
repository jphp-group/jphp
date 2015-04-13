package org.develnext.jphp.ext.libgdx.classes.graphics;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.develnext.jphp.ext.libgdx.LibGDXExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

import static php.runtime.annotation.Reflection.Name;
import static php.runtime.annotation.Reflection.WrapInterface;

@Name(LibGDXExtension.NAMESPACE + "graphics\\SpriteBatch")
public class WrapSpriteBatch extends WrapBatch {
    public WrapSpriteBatch(Environment env, SpriteBatch wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapSpriteBatch(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new SpriteBatch();
    }

    @Override
    public SpriteBatch getWrappedObject() {
        return (SpriteBatch) super.getWrappedObject();
    }
}
