package org.develnext.jphp.ext.libgdx.classes;


import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import org.develnext.jphp.ext.libgdx.LibGDXExtension;
import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

import static php.runtime.annotation.Reflection.Name;
import static php.runtime.annotation.Reflection.Signature;

@Name(LibGDXExtension.NAMESPACE + "LwjglApplication")
public class WrapLwjglApplication extends WrapApplication {
    public WrapLwjglApplication(Environment env, LwjglApplication wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapLwjglApplication(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct(ApplicationListener listener, LwjglApplicationConfiguration configuration) {
        this.__wrappedObject = new LwjglApplication(listener, configuration);
    }
}
