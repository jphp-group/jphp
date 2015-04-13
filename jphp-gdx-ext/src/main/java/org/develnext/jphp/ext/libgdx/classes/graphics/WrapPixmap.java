package org.develnext.jphp.ext.libgdx.classes.graphics;


import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import org.develnext.jphp.ext.libgdx.LibGDXExtension;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.annotation.Reflection.WrapInterface;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;


@Name(LibGDXExtension.NAMESPACE + "graphics\\Pixmap")
@WrapInterface(value = Pixmap.class, skipConflicts = true)
public class WrapPixmap extends BaseWrapper<Pixmap> {
    public WrapPixmap(Environment env, Pixmap wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapPixmap(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct(int width, int height, Pixmap.Format format) {
        __wrappedObject = new Pixmap(width, height, format);
    }

    @Signature
    public static Pixmap ofFile(FileHandle fileHandle) {
        return new Pixmap(fileHandle);
    }
}
