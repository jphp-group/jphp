package org.develnext.jphp.ext.gdx.classes;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import org.develnext.jphp.ext.gdx.GdxExtension;
import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.memory.ArrayMemory;
import php.runtime.reflection.ClassEntity;

@Reflection.Name("LwjglApplication")
@Reflection.Namespace(GdxExtension.NS)
public class PLwjglApplication extends BaseObject {
    public PLwjglApplication(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Reflection.Signature
    public static PGdxGame run(Environment env, PGdxGame game, ArrayMemory config) {
        LwjglApplicationConfiguration _config = config.toBean(env, LwjglApplicationConfiguration.class);

        new LwjglApplication(game.createListener(), _config);
        return game;
    }
}
