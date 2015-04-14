package php.runtime.ext.core.classes;

import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.annotation.Reflection.UseJavaLikeNames;
import php.runtime.env.CompileScope;
import php.runtime.env.Environment;
import php.runtime.ext.CoreExtension;
import php.runtime.ext.support.Extension;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

@UseJavaLikeNames
@Reflection.Name(CoreExtension.NAMESPACE + "lang\\Extension")
abstract public class WrapExtension extends BaseWrapper<Extension> implements Extension.Extensible {
    public WrapExtension(Environment env, Extension wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapExtension(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public void __construct() {
        __wrappedObject = new Extension() {
            @Override
            public Status getStatus() {
                return WrapExtension.this.getStatus();
            }

            @Override
            public void onRegister(CompileScope scope) {
                //WrapExtension.this.onRegister();
            }

            @Override
            public void onLoad(Environment env) {
                WrapExtension.this.onLoad(new WrapEnvironment(env, env));
            }
        };
    }

    @Override
    public Extension getExtension() {
        return getWrappedObject();
    }

    @Signature
    abstract public Extension.Status getStatus();

    @Signature
    abstract public void onLoad(WrapEnvironment env);
}
