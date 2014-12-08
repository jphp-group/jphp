package org.develnext.jphp.swing.classes;

import org.develnext.jphp.swing.Scope;
import org.develnext.jphp.swing.SwingExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.lang.IObject;
import php.runtime.reflection.ClassEntity;

@Name(SwingExtension.NAMESPACE + "ScopeValue")
final public class WrapScopeValue extends BaseObject {
    private Scope scope;
    private String name;

    public WrapScopeValue(Environment env, Scope scope, String name) {
        super(env);
        this.scope = scope;
        this.name = name;
    }

    public WrapScopeValue(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public IObject __get(Environment env, String name) {
        return new WrapScopeValue(env, scope, this.name + '.' + name);
    }

    @Signature
    public void __set(Environment env, String name, Memory value) throws Throwable {
        scope.set(env, this.name + '.' + name, value);
    }

    @Signature
    public Memory getValue(Environment env) {
        return scope.get(env, name);
    }

    @Signature
    public void setValue(Environment env, Memory value) throws Throwable {
        scope.set(env, name, value);
    }

    @Signature
    public void bind(Environment env, IObject object, String property) {
        scope.bind(env, name, object, property);
    }

    @Signature
    private void __construct() { }
}
