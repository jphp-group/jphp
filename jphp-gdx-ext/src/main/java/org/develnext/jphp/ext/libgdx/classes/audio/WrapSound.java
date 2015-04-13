package org.develnext.jphp.ext.libgdx.classes.audio;

import com.badlogic.gdx.audio.Sound;
import org.develnext.jphp.ext.libgdx.LibGDXExtension;
import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

import static php.runtime.annotation.Reflection.Name;
import static php.runtime.annotation.Reflection.WrapInterface;

@Name(LibGDXExtension.NAMESPACE + "audio\\Sound")
@WrapInterface(value = Sound.class, skipConflicts = true)
public class WrapSound extends BaseWrapper<Sound> {
    public WrapSound(Environment env, Sound wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapSound(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Reflection.Signature
    private void __construct() { }
}
