package org.develnext.jphp.ext.libgdx.classes;


import com.badlogic.gdx.*;
import org.develnext.jphp.ext.libgdx.LibGDXExtension;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.reflection.ClassEntity;

import static php.runtime.annotation.Reflection.Name;
import static php.runtime.annotation.Reflection.Signature;

@Name(LibGDXExtension.NAMESPACE + "Gdx")
public class WrapGdx extends BaseObject {
    public WrapGdx(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    private void __construct() { }

    @Signature
    public static Files files() {
        return Gdx.files;
    }

    @Signature
    public static Graphics graphics() {
        return Gdx.graphics;
    }

    @Signature
    public static Application app() {
        return Gdx.app;
    }

    @Signature
    public static Input input() {
        return Gdx.input;
    }

    @Signature
    public static Audio audio() {
        return Gdx.audio;
    }
}
