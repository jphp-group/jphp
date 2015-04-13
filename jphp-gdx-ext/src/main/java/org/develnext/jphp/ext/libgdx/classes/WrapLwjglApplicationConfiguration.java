package org.develnext.jphp.ext.libgdx.classes;

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import org.develnext.jphp.ext.libgdx.LibGDXExtension;
import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

import static php.runtime.annotation.Reflection.Name;
import static php.runtime.annotation.Reflection.Signature;

@Name(LibGDXExtension.NAMESPACE + "LwjglApplicationConfiguration")
@Reflection.WrapInterface(value = LwjglApplicationConfiguration.class, skipConflicts = true, wrapFields = true)
public class WrapLwjglApplicationConfiguration extends BaseWrapper<LwjglApplicationConfiguration> {
    public WrapLwjglApplicationConfiguration(Environment env, LwjglApplicationConfiguration wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapLwjglApplicationConfiguration(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new LwjglApplicationConfiguration();
    }
}
