package org.develnext.jphp.ext.libgdx.classes.audio;

import com.badlogic.gdx.audio.AudioDevice;
import org.develnext.jphp.ext.libgdx.LibGDXExtension;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

import static php.runtime.annotation.Reflection.*;

@Name(LibGDXExtension.NAMESPACE + "audio\\AudioDevice")
@WrapInterface(value = AudioDevice.class, skipConflicts = true)
public class WrapAudioDevice extends BaseWrapper<AudioDevice> {
    public WrapAudioDevice(Environment env, AudioDevice wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapAudioDevice(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void writeFloatSamples(float[] samples, int offset, int numSamples) {
        __wrappedObject.writeSamples(samples, offset, numSamples);
    }

    @Signature
    private void __construct() { }
}
