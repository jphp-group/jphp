package org.develnext.jphp.ext.libgdx.classes;

import com.badlogic.gdx.Audio;
import org.develnext.jphp.ext.libgdx.LibGDXExtension;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

import static php.runtime.annotation.Reflection.*;

@Name(LibGDXExtension.NAMESPACE + "Audio")
@WrapInterface(value = Audio.class, skipConflicts = true)
public class WrapAudio extends BaseWrapper<Audio> {
    public WrapAudio(Environment env, Audio wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapAudio(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    private void __construct() { }
}
