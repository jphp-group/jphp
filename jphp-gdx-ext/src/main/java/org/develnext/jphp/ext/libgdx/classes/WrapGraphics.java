package org.develnext.jphp.ext.libgdx.classes;


import com.badlogic.gdx.Graphics;
import org.develnext.jphp.ext.libgdx.LibGDXExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Name;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

@Name(LibGDXExtension.NAMESPACE + "Graphics")
@Reflection.WrapInterface(value = Graphics.class, skipConflicts = true)
public class WrapGraphics extends BaseWrapper<Graphics> {
    public WrapGraphics(Environment env, Graphics wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapGraphics(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }
}
