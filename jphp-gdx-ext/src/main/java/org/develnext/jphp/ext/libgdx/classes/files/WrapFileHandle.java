package org.develnext.jphp.ext.libgdx.classes.files;

import com.badlogic.gdx.files.FileHandle;
import org.develnext.jphp.ext.libgdx.LibGDXExtension;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.memory.ArrayMemory;
import php.runtime.reflection.ClassEntity;

import java.io.File;
import java.util.*;

import static php.runtime.annotation.Reflection.Name;
import static php.runtime.annotation.Reflection.Signature;
import static php.runtime.annotation.Reflection.WrapInterface;

@Name(LibGDXExtension.NAMESPACE + "files\\FileHandle")
@WrapInterface(value = FileHandle.class, skipConflicts = true)
public class WrapFileHandle extends BaseWrapper<FileHandle> {
    public WrapFileHandle(Environment env, FileHandle wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapFileHandle(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct(File path) {
        __wrappedObject = new FileHandle(path);
    }

    @Signature
    public List<FileHandle> getList() {
        return Arrays.asList(__wrappedObject.list());
    }

    @Signature
    public List<FileHandle> getList(String suffix) {
        return Arrays.asList(__wrappedObject.list(suffix));
    }

    @Signature
    public String __toString() {
        return __wrappedObject.toString();
    }

    @Signature
    public Memory __debugInfo(Environment env, Memory... args) {
        ArrayMemory r = new ArrayMemory();
        r.refOfIndex("*path").assign(__wrappedObject.toString());

        return r.toConstant();
    }
}
