package com.example;

import php.runtime.ext.NetExtension;
import org.develnext.jphp.swing.SwingExtension;
import org.develnext.jphp.zend.ext.ZendExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.CompileScope;
import php.runtime.env.Environment;
import php.runtime.ext.support.Extension;
import php.runtime.lang.BaseWrapper;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

public class ExampleExtension extends Extension {
    @Override
    public String getName() {
        return "Example";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String[] getRequiredExtensions() {
        return new String[] {
                SwingExtension.class.getName(),
                ZendExtension.class.getName()
        };
    }

    @Override
    public void onRegister(CompileScope scope) {
        // ...
        registerWrapperClass(scope, Memory.class, Foobar.class);
    }

    @WrapInterface(Foobar.Methods.class)
    public static class Foobar extends BaseWrapper<Memory> {
        interface Methods {
            @Property int pointer();
        }

        public Foobar(Environment env, ClassEntity clazz) {
            super(env, clazz);
        }

        public Foobar(Environment env, Memory wrappedObject) {
            super(env, wrappedObject);
        }

        @Signature
        public void __construct() {
            __wrappedObject = StringMemory.valueOf("Yes!!!");
        }
    }
}
