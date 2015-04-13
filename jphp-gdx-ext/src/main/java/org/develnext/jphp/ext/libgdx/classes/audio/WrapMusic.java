package org.develnext.jphp.ext.libgdx.classes.audio;

import com.badlogic.gdx.audio.Music;
import org.develnext.jphp.ext.libgdx.LibGDXExtension;
import php.runtime.env.Environment;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

import static php.runtime.annotation.Reflection.*;

@Name(LibGDXExtension.NAMESPACE + "audio\\Music")
@WrapInterface(value = Music.class, skipConflicts = true)
public class WrapMusic extends BaseWrapper<Music> {
    public WrapMusic(Environment env, Music wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapMusic(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    private void __construct() { }

    @Signature
    public void setOnCompletionListener(@Nullable final Invoker invoker) {
        if (invoker == null) {
            __wrappedObject.setOnCompletionListener(null);
        } else {
            __wrappedObject.setOnCompletionListener(new Music.OnCompletionListener() {
                @Override
                public void onCompletion(Music music) {
                    invoker.callAny(music);
                }
            });
        }
    }
}
