package org.develnext.jphp.ext.pna.classes;

import org.develnext.jphp.ext.pna.PhpNativeInterfaceExtension;
import org.develnext.jphp.ext.pna.support.NativeClassEntity;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.memory.ArrayMemory;
import php.runtime.reflection.ClassEntity;

@Reflection.Namespace(PhpNativeInterfaceExtension.NS)
public class NativeObject extends BaseWrapper {
    public NativeObject(Environment env, ClassEntity entity, Object wrappedObject) {
        super(env, entity);
        __wrappedObject = wrappedObject;
    }

    public NativeObject(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public Class<?> getRawClass() {
        return ((NativeClassEntity) getReflection()).getRawClass();
    }

    @Reflection.Signature
    public Memory __debugInfo() {
        return ArrayMemory.ofPair("*nativeClass", getRawClass().getName());
    }

    public void __setNativeObject(Object object) {
        __wrappedObject = object;
    }
}
