package org.develnext.jphp.ext.libgdx.classes.graphics;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import org.develnext.jphp.ext.libgdx.LibGDXExtension;
import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

import static php.runtime.annotation.Reflection.Name;
import static php.runtime.annotation.Reflection.Signature;
import static php.runtime.annotation.Reflection.WrapInterface;

@Name(LibGDXExtension.NAMESPACE + "graphics\\Sprite")
@WrapInterface(value = Sprite.class, skipConflicts = true)
public class WrapSprite extends BaseWrapper<Sprite> {
    public WrapSprite(Environment env, Sprite wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapSprite(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new Sprite();
    }

    @Signature
    public void __construct(Texture texture) {
        __wrappedObject = new Sprite(texture);
    }

    @Signature
    public void __construct(Texture texture, int width, int height) {
        __wrappedObject = new Sprite(texture, width, height);
    }

    @Signature
    public void __construct(Texture texture, int width, int height, int x, int y) {
        __wrappedObject = new Sprite(texture, x, y, width, height);
    }
}
