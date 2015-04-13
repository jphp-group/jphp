package org.develnext.jphp.ext.libgdx.classes.audio;

import com.badlogic.gdx.audio.AudioRecorder;
import org.develnext.jphp.ext.libgdx.LibGDXExtension;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

import static php.runtime.annotation.Reflection.*;

@Name(LibGDXExtension.NAMESPACE + "audio\\AudioRecorder")
@WrapInterface(value = AudioRecorder.class, skipConflicts = true)
public class WrapAudioRecorder extends BaseWrapper<AudioRecorder> {
    public WrapAudioRecorder(Environment env, AudioRecorder wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapAudioRecorder(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    private void __construct() { }
}
