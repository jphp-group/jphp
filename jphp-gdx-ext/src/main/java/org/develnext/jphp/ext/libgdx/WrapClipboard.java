package org.develnext.jphp.ext.libgdx;

import com.badlogic.gdx.utils.Clipboard;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.annotation.Reflection.WrapInterface;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

@Name(LibGDXExtension.NAMESPACE + "Clipboard")
@WrapInterface(value = Clipboard.class, skipConflicts = true)
public class WrapClipboard extends BaseWrapper<Clipboard> {
    public WrapClipboard(Environment env, Clipboard wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapClipboard(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    private void __construct() { }
}
