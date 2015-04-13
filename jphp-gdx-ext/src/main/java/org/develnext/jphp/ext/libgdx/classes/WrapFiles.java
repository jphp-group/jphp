package org.develnext.jphp.ext.libgdx.classes;


import com.badlogic.gdx.Files;
import org.develnext.jphp.ext.libgdx.LibGDXExtension;
import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

import static php.runtime.annotation.Reflection.Name;
import static php.runtime.annotation.Reflection.Signature;
import static php.runtime.annotation.Reflection.WrapInterface;

@Name(LibGDXExtension.NAMESPACE + "Files")
@WrapInterface(value = Files.class, skipConflicts = true)
public class WrapFiles extends BaseWrapper<Files> {
    public WrapFiles(Environment env, Files wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapFiles(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    private void __construct() { }
}
