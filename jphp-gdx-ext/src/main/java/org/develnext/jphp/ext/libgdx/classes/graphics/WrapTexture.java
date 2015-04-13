package org.develnext.jphp.ext.libgdx.classes.graphics;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import org.develnext.jphp.ext.libgdx.LibGDXExtension;
import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

import static php.runtime.annotation.Reflection.Name;
import static php.runtime.annotation.Reflection.Signature;

@Name(LibGDXExtension.NAMESPACE + "graphics\\Texture")
@Reflection.WrapInterface(value = Texture.class, skipConflicts = true)
public class WrapTexture extends BaseWrapper<Texture> {
    public WrapTexture(Environment env, Texture wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapTexture(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct(Pixmap pixmap) {
        __wrappedObject = new Texture(pixmap);
    }

    @Signature
    public void __construct(Pixmap pixmap, boolean useMipMaps) {
        __wrappedObject = new Texture(pixmap, useMipMaps);
    }

    @Signature
    public void __construct(Pixmap pixmap, boolean useMipMaps, Pixmap.Format format) {
        __wrappedObject = new Texture(pixmap, format, useMipMaps);
    }

    @Signature
    public static Texture ofFile(FileHandle fileHandle) {
        return new Texture(fileHandle);
    }

    @Signature
    public static Texture ofFile(FileHandle fileHandle, boolean useMipMaps) {
        return new Texture(fileHandle, useMipMaps);
    }

    @Signature
    public static Texture ofFile(FileHandle fileHandle, boolean useMipMaps, Pixmap.Format format) {
        return new Texture(fileHandle, format, useMipMaps);
    }
}
