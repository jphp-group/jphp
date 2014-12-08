package org.develnext.jphp.swing.classes;

import org.develnext.jphp.swing.Scope;
import org.develnext.jphp.swing.SwingExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.lang.IObject;
import php.runtime.reflection.ClassEntity;

@Name(SwingExtension.NAMESPACE + "Scope")
public class WrapScope extends BaseWrapper<Scope> {
    public WrapScope(Environment env, Scope wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapScope(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new Scope();
    }

    @Signature
    public void __set(Environment env, String name, Memory value) throws Throwable {
        getWrappedObject().set(env, name, value);
    }

    @Signature
    public IObject __get(Environment env, String name) {
        return new WrapScopeValue(env, getWrappedObject(), name);
    }

    @Signature
    public static Scope getDefault() {
        return SwingExtension.scope;
    }
}
