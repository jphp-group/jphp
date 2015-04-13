package org.develnext.jphp.ext.libgdx.classes;

import com.badlogic.gdx.Input;
import org.develnext.jphp.ext.libgdx.LibGDXExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.annotation.Reflection.WrapInterface;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

@Name(LibGDXExtension.NAMESPACE + "Input")
@WrapInterface(value = Input.class, skipConflicts = true)
final public class WrapInput extends BaseWrapper<Input> {
    public WrapInput(Environment env, Input wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapInput(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    private void __construct() { }
}
