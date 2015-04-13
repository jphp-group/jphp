package org.develnext.jphp.ext.libgdx.classes.graphics;

import com.badlogic.gdx.Graphics;
import org.develnext.jphp.ext.libgdx.LibGDXExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.memory.ArrayMemory;
import php.runtime.reflection.ClassEntity;


import static php.runtime.annotation.Reflection.Name;
import static php.runtime.annotation.Reflection.WrapInterface;

@Name(LibGDXExtension.NAMESPACE + "graphics\\DisplayMode")
@WrapInterface(value = Graphics.DisplayMode.class, skipConflicts = true)
public class WrapDisplayMode extends BaseWrapper<Graphics.DisplayMode> {
    public WrapDisplayMode(Environment env, Graphics.DisplayMode wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapDisplayMode(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public Memory __debugInfo(Environment env, Memory... args) {
        ArrayMemory r = new ArrayMemory();
        r.refOfIndex("*width").assign(__wrappedObject.width);
        r.refOfIndex("*height").assign(__wrappedObject.height);
        r.refOfIndex("*bitsPerPixel").assign(__wrappedObject.bitsPerPixel);
        r.refOfIndex("*refreshRate").assign(__wrappedObject.refreshRate);

        return r.toConstant();
    }

    @Signature
    public int getWidth() {
        return __wrappedObject.width;
    }

    @Signature
    public int getHeight() {
        return __wrappedObject.height;
    }

    @Signature
    public int getBitsPerPixel() {
        return __wrappedObject.bitsPerPixel;
    }

    @Signature
    public int getRefreshRate() {
        return __wrappedObject.refreshRate;
    }

    @Signature
    public String __toString() {
        return __wrappedObject.toString();
    }
}
